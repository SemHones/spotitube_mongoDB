package datasource.dao;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import datasource.util.DatabaseProperties;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultDAO {

    protected static final Logger LOGGER = Logger.getLogger(DefaultDAO.class.getName());
    protected static final String DATABASE_NAME = "spotitube";
    protected DatabaseProperties databaseProperties;

    private MongoClient mongoClient;

    public DefaultDAO() {
        this.databaseProperties = new DatabaseProperties();
    }

    protected MongoCollection<Document> openConnection(String collectionName){
        mongoClient = MongoClients.create(databaseProperties.connectionString());
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
        return database.getCollection(collectionName);
    }

    protected void closeConnections(){
        try {
            if(mongoClient != null){mongoClient.close();}
        } catch (MongoException me) {
            LOGGER.log(Level.SEVERE, me.toString(), me);
        }
    }

    protected void logError(MongoException me){
        LOGGER.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionString(), me);
    }
}
