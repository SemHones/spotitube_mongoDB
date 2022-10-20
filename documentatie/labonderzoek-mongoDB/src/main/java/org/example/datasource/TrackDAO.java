package org.example.datasource;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.datasource.util.DatabaseProperties;
import org.example.domain.Track;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class TrackDAO {

    private DatabaseProperties databaseProperties = new DatabaseProperties();
    private static final String DATABASE_NAME = "labonderzoek";
    private static final String COLLECTION_NAME = "tracks";


    public Track findFirst() {
        MongoCollection<Document> collection = getCollection(COLLECTION_NAME);

        FindIterable<Document> cursor = collection.find();
        return documentToTrack(cursor.first());
    }

    public List<Track> findAll() {
        MongoCollection<Document> collection = getCollection(COLLECTION_NAME);
        List<Track> tracks = new ArrayList<>();

        FindIterable<Document> cursor = collection.find();
        for (Document object : cursor){
            tracks.add(documentToTrack(object));
        }
        return tracks;
    }

    public void addTrack(Track track) {
        MongoCollection<Document> collection = getCollection(COLLECTION_NAME);

        try {
            collection.insertOne(trackToDocument(track));
            System.out.println("Inserted document: " + track.toString());
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }

    }

    public void deleteItem(String id) {
        MongoCollection<Document> collection = getCollection(COLLECTION_NAME);

        Bson query = eq("_id", id);
        try {
            DeleteResult result = collection.deleteOne(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }
    }

    public MongoCollection<Document> getCollection(String collectionName){
        MongoClient mongoClient = MongoClients.create(databaseProperties.connectionString());
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        return database.getCollection(collectionName);
    }

    public Document trackToDocument(Track track){
        return new Document(new BasicDBObject("_id", track.getId())
            .append("title", track.getTitle())
            .append("performer", track.getPerformer())
            .append("duration", track.getDuration())
            .append("album", track.getAlbum())
            .append("playcount", track.getPlaycount())
            .append("publicationDate", track.getPublicationDate())
            .append("description", track.getDescription())
            .append("offlineAvailable", track.getOfflineAvailable()).toMap());
    }

    public Track documentToTrack(Document cursor){
        return new Track(
            (String)cursor.get("_id"),
            (String)cursor.get("title"),
            (String)cursor.get("performer"),
            (int)cursor.get("duration"),
            (String)cursor.get("album"),
            (int)cursor.get("playcount"),
            (String)cursor.get("publicationDate"),
            (String)cursor.get("description"),
            (boolean)cursor.get("offlineAvailable")
        );
    }
}
