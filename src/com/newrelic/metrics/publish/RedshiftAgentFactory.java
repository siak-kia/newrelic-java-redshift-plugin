package com.newrelic.metrics.publish;


import com.newrelic.metrics.publish.configuration.Config;
import com.newrelic.metrics.publish.configuration.ConfigurationException;
import org.json.simple.JSONArray;

import java.util.Map;

public class RedshiftAgentFactory extends AgentFactory {

    @Override
    public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
        String GUID = (String) properties.get("GUID");
        String version = (String) properties.get("version");
        String name = (String) properties.get("name");
        String host = (String) properties.get("host");
        String port = (String)properties.get("port");
        String DBName = (String) properties.get("dbname");
        String username = (String) properties.get("username");
        String password = (String) properties.get("password");




        if (   version == null || name == null || host == null || port == null || DBName ==null
                || username == null || password == null) {
            throw new ConfigurationException("'name', 'host', 'port', 'DBName', 'username ' and password' cannot be null.");
        }

        return new RedshiftAgent(GUID, version, name, host, port, DBName,username, password);

    }
}

