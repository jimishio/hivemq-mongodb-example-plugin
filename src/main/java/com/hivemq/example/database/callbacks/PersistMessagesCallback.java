package com.hivemq.example.database.callbacks;

import com.hivemq.example.database.DBPlugin;
import com.hivemq.example.models.Constants;
import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.events.OnPublishReceivedCallback;
import com.hivemq.spi.callback.exception.OnPublishReceivedException;
import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;
import com.hivemq.spi.services.PluginExecutorService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * @author Jimish Parekh
 */
public class PersistMessagesCallback implements OnPublishReceivedCallback {

    private static Logger log = LoggerFactory.getLogger(PersistMessagesCallback.class);
    private Provider<Connection> connectionProvider;
    private PluginExecutorService pluginExecutorService;
    
    private DBPlugin dbPlugin;

    private static final String SQLStatement = "INSERT INTO `Messages` (message,topic,qos,client) VALUES (?,?,?,?)";


    @Inject
    public PersistMessagesCallback(final PluginExecutorService pluginExecutorService) {
//        this.connectionProvider = connectionProvider;
        this.pluginExecutorService = pluginExecutorService;
    }

    @Override
    public void onPublishReceived(final PUBLISH publish, final ClientData clientData) throws OnPublishReceivedException {
//        final Connection connection = connectionProvider.get();

        pluginExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                	dbPlugin = new DBPlugin();
                	MongoClient mongo = new MongoClient("localhost", 27017);
                	//DB db = dbPlugin.getDb();
                	DB db = mongo.getDB("hivemq");
                	if(db != null){
                		log.error("not null db");
                		DBCollection table = db.getCollection(Constants.MESSAGE_TABLE_NAME);
                		BasicDBObject document = new BasicDBObject();
                    	document.put(Constants.MESSAGES_ID, publish.getMessageId());
                    	String message = new String(publish.getPayload());
                    	document.put(Constants.MESSAGES_MESSAGE, message);
                    	document.put(Constants.MESSAGES_QOS, publish.getQoS().getQosNumber());
                    	document.put(Constants.MESSAGES_TOPIC, publish.getTopic());
                    	document.put(Constants.MESSAGES_CLIENT, clientData.getClientId());
                    	table.insert(document);
                	} else {
                		log.error("db is null");
                	}
                	
                	
                	
                	
                	System.out.println("Done");
                } catch (Exception e) {
                    log.error("An error occured while preparing the SQL statement"+e.toString(), e);
                } finally {
                    try {
//                        connection.close();
                    } catch (Exception e) {
                        log.error("An error occured while giving back a connection to the connection pool");
                    }
                }
            }
        });

    }

    @Override
    public int priority() {
        return CallbackPriority.HIGH;
    }
    
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
}
