/*
 * ShardingJdbc - this use to get connect for sharding jdbc
 *
 * Copyright (C) 2021 justbk
 *
 */


import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ShardingJdbc {
    private static org.apache.log4j.Logger log = Logger.getLogger(ShardingJdbc.class);
    public static final String DEFAULT_CONFIG = "config-sharding.yaml";
    public static  ConcurrentHashMap<String, DataSource> mapDataSource = new ConcurrentHashMap<>(1);
    public static Object createDataSourceLock = new int[0];
    
    
    private static DataSource loadShardingDataSource(File file) {
        final String factoryClass = "org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory";
        final String createDataSourceMethodName = "createDataSource";
        try {
            Class<?> ssDataSourceFactory = Class.forName(factoryClass);
            Method createDataSourceMethod = ssDataSourceFactory.getMethod(createDataSourceMethodName, File.class);
            return (DataSource) createDataSourceMethod.invoke(null, file);
        } catch (Exception sqlExp) {
            sqlExp.printStackTrace();
        }
        return null;
    }

    public static DataSource getDataSource(String configFile) {
        if (mapDataSource.containsKey(configFile)) {
            return mapDataSource.get(configFile);
        }
        synchronized (createDataSourceLock) {
            if (mapDataSource.containsKey(configFile)) {
                return mapDataSource.get(configFile);
            }
            if (configFile == null || "".equals(configFile)) {
                configFile = DEFAULT_CONFIG;
            }
            File file = new File(configFile);
            if (!file.isAbsolute()) {
                String fileAbsPath = ShardingJdbc.class.getClassLoader().getResource(configFile).getFile();
                file = new File(fileAbsPath);
            }
            try {
                DataSource dataSource =  loadShardingDataSource(file);
                mapDataSource.put(configFile, dataSource);
                mapDataSource.put(file.getAbsolutePath(), dataSource);
                return dataSource;
            } catch (Exception exp) {
                exp.printStackTrace();
                return null;
            }
        }

    }

    public static synchronized Connection getConnection(String dbConn, Properties dbProperties) throws SQLException {
        if (dbConn.toLowerCase(Locale.ENGLISH).contains("sharding:")) {
            log.info("create in SHARDING!!!" + dbConn);
            DataSource dataSource = getDataSource((String) dbProperties.getOrDefault("config", ""));
            return dataSource.getConnection();
        } else {
            String newConn = PropertiesFactory.instance.getConnection(dbConn);
            return DriverManager.getConnection(newConn, dbProperties);
        }
    }
}
