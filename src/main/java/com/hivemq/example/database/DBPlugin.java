package com.hivemq.example.database;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DBPlugin {
	private static Logger log = LoggerFactory.getLogger(DBPlugin.class);
	
	MongoClient mongo;
	DB db;
	
	public DBPlugin(){
		try{
			MongoClient mongo = new MongoClient("127.0.0.1", 27017);
			DB db = mongo.getDB("hivemq");
			log.info("initializing database");
		} catch (UnknownHostException e){
			//db = null;
			log.error("error occurred while fetching db", e);
		}
		
	}
	
	public DB getDb(){
		try{
			MongoClient mongo = new MongoClient("127.0.0.1", 27017);
			DB db = mongo.getDB("hivemq");
			log.info("initializing database");
		} catch (UnknownHostException e){
			//db = null;
			log.error("error occurred while fetching db", e);
		}
		return db;
	}

}
