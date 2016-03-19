package com.hivemq.example.database;

import com.google.inject.Provides;
import com.hivemq.example.database.callbacks.PersistMessagesCallback;
import com.hivemq.spi.HiveMQPluginModule;
import com.hivemq.spi.PluginEntryPoint;
import com.hivemq.spi.plugin.meta.Information;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jimish Parekh
 */
@Information(
        name = "Database Example Plugin",
        version = "3.0",
        author = "dc-square GmbH",
        description = "A example plugin which persists every message to the database and authenticates clients from the database")
public class DatabaseExamplePluginModule extends HiveMQPluginModule {
	
	private static final String TAG = "DatabaseExamplePluginModule";
	private static Logger log = LoggerFactory.getLogger(DatabaseExamplePlugin.class);


//    @Provides
    public DB provideConnection(final DB db) throws SQLException {
    	
        return db;
    }

//    @Provides
//    @Singleton
    public DB provideConnectionPool() {

        //See https://github.com/brettwooldridge/HikariCP

//        final HikariConfig config = new HikariConfig();
//        config.setMaximumPoolSize(15);
//        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//        config.addDataSourceProperty("serverName", "localhost");
//        config.addDataSourceProperty("port", "3306");
//        config.addDataSourceProperty("databaseName", "HiveMQ3");
//        config.addDataSourceProperty("user", "root");
//        config.addDataSourceProperty("password", "mysql123");
//
//        //See https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
//
//        config.addDataSourceProperty("cachePrepStmts", true);
//        config.addDataSourceProperty("prepStmtCacheSize", 250);
//        config.addDataSourceProperty("useServerPrepStmts", true);
//
//        return new HikariDataSource(config);
    	/**** Connect to MongoDB ****/
		// Since 2.10.0, uses MongoClient
		MongoClient mongo;
		DB db;
		try {
			mongo = new MongoClient("localhost", 27017);
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			db = mongo.getDB("testdb");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			db = null;
			log.error(TAG, "MongoClient Unknown host exception occurred.");
		}

		
		return db;
    }

    @Override
    protected void configurePlugin() {
    }

    @Override
    protected Class<? extends PluginEntryPoint> entryPointClass() {
        return DatabaseExamplePlugin.class;
    }
}