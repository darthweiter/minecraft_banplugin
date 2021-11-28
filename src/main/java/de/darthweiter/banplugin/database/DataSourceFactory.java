package de.darthweiter.banplugin.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.darthweiter.banplugin.BanPlugin;
import de.darthweiter.banplugin.configuration.Configuration;
import de.darthweiter.banplugin.util.FileIO;
import de.darthweiter.banplugin.util.Util;

import javax.sql.DataSource;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataSourceFactory {

    private static final String MYSQL_URL = "mysql.url";
    private static final String MYSQL_USERNAME = "mysql.username";
    private static final String MYSQL_PASSWORD = "mysql.password";
    private static final Map<String, String> databaseMap = new LinkedHashMap<>();

    private static final String FILE_NAME = "db.properties";
    private static final MysqlDataSource MYSQL_DATA_SOURCE = new MysqlDataSource();

    private DataSourceFactory() {
    }

    public static void generateDBFile() {
        initDatabaseMap();
        File directory = FileIO.loadDirectory("database");
        if (directory != null) {
            File databasePropertiesFile = new File(directory.getAbsolutePath() + File.separator + FILE_NAME);
            if (databasePropertiesFile.exists()) {
                Util.updateMap(databasePropertiesFile, databaseMap);
            }
            FileIO.createFile(directory, FILE_NAME, databaseMap);
        } else {
            BanPlugin.log(Configuration.getErrorMsgLoadProperties() + ": " + FILE_NAME);
        }
        MYSQL_DATA_SOURCE.setURL(databaseMap.get(MYSQL_URL));
        MYSQL_DATA_SOURCE.setUser(databaseMap.get(MYSQL_USERNAME));
        MYSQL_DATA_SOURCE.setPassword(databaseMap.get(MYSQL_PASSWORD));
    }

    public static DataSource getMySQLDataSource() {
        return MYSQL_DATA_SOURCE;
    }

    private static void initDatabaseMap() {
        databaseMap.put(MYSQL_URL, "The Url to the database");
        databaseMap.put(MYSQL_USERNAME, "The Username to connect to the Mysql-Database");
        databaseMap.put(MYSQL_PASSWORD, "The Password to connect to the Mysql-Database");
    }
}
