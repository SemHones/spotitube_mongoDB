package datasource.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import datasource.objects.User;
import org.bson.Document;
import org.bson.conversions.Bson;


import static com.mongodb.client.model.Filters.eq;

public class UserDAO extends DefaultDAO{

    private static final String COLLECTION_NAME = "user";

    private User user;

    public UserDAO(){ super(); }

    public User getSingleUser(String username, String password){
        this.user = new User("", "", "", "");
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Bson byUsername = eq("username", username);
            Bson byPassword = eq("password", password);
            FindIterable<Document> cursor = collection.find(byUsername);
            return documentToUser(cursor.first());

        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
        return this.user;
    }

    public User getSingleUserByToken(String token){
        this.user = new User("", "", "", "");
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Bson byToken = eq("token", token);
            FindIterable<Document> cursor = collection.find(byToken);
            return documentToUser(cursor.first());

        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
        return this.user;
    }

    private Document userToDocument(User user){
        return new Document(new BasicDBObject("_id", user.getId())
                .append("username", user.getUsername())
                .append("token", user.getToken())
                .append("password", user.getPassword()).toMap());
    }

    private User documentToUser(Document cursor){
        return new User(
            (String)cursor.get("_id"),
            (String)cursor.get("username"),
            (String)cursor.get("token"),
            (String)cursor.get("password")
        );
    }
}
