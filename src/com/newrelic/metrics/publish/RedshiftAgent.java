package com.newrelic.metrics.publish;

import com.newrelic.metrics.publish.util.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class RedshiftAgent extends Agent{

    private static final Logger logger = Logger.getLogger(RedshiftAgent.class);
    private String name;
    private String host;
    private String port;
    private String DBName;
    private String username;
    private String password;





    public RedshiftAgent(String GUID,
                         String version,
                         String name,
                         String host,
                         String port,
                         String DBName,
                         String username,
                         String password) {
        super(GUID, version);
        this.name = name;
        this.host = host;
        this.port = port;
        this.DBName = DBName;
        this.username = username;
        this.password = password;
    }



    @Override
    public void pollCycle() {

         String memoryUsed;
         String numberOfConnections;


        String queryForMemoryUsed = "SELECT (SUM(used)/1024.00) AS memory_used\n" +
                                    "FROM    stv_partitions\n" +
                                    "WHERE   part_begin=0";

        memoryUsed =calculateMetric(queryForMemoryUsed, "memory_used");
        reportMetric("Disk Space", "GB", Math.round(Float.parseFloat(memoryUsed)));


        String queryForNumberOfConnections = "SELECT count(*) AS database_connections\n" +
                                             "FROM stv_sessions;";
        numberOfConnections =calculateMetric(queryForNumberOfConnections, "database_connections");
        reportMetric("Number of Connections", "", Math.round(Float.parseFloat(numberOfConnections)));



    }



    @Override
    public String getAgentName() {
        return this.name;
    }

    @Override
    public void setupMetrics() {
        super.setupMetrics();
    }

    @Override
    public String getGUID() {
        return super.getGUID();
    }

    @Override
    public String getVersion() {
        return super.getVersion();
    }

    @Override
    public void prepareToRun() {
        super.prepareToRun();
    }




    private String calculateMetric(String query, String resultName) {
        Connection connection = getConnection();
        Statement statement;
        String result = null;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();


            resultSet = statement.executeQuery(query);

            //Get the data from the result set.
            if (resultSet.next()) {
                result = resultSet.getString(resultName);
                logger.debug("result" + result);

            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    private Connection getConnection() {
        final String dbURL = "jdbc:postgresql://" + host + ":" + port + "/" + DBName;
        final String MasterUsername = username;
        final String MasterUserPassword = password;


        Connection connection = null;
        try {

            Class.forName("com.amazon.redshift.jdbc41.Driver");

            logger.debug("connecting to database");
            Properties props = new Properties();
            props.setProperty("user", MasterUsername);
            props.setProperty("password", MasterUserPassword);
            connection = DriverManager.getConnection(dbURL, props);


        } catch(Exception ex){

        ex.printStackTrace();
    }
        return connection;
    }



}

