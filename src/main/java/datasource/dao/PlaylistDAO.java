package datasource.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import datasource.objects.Playlist;
import datasource.objects.Track;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class PlaylistDAO extends DefaultDAO{

    private static final String COLLECTION_NAME = "playlist";

    public PlaylistDAO(){
        super();
    }

    public Playlist getPlaylist(String playlistId){
        Playlist emptyPlaylist = new Playlist("0", "");
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Bson query = eq("_id", playlistId);
            FindIterable<Document> cursor = collection.find(query);

            List<Track> tracks = convertDocumentPlayListTracks(cursor.first());
            Playlist playlist = documentToPlaylist(cursor.first());
            playlist.setTracks(tracks);
            return playlist;

        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
        return emptyPlaylist;
    }

    public List<Playlist> getAllPlaylistFromUser(String userId){
        List<Playlist> playlists = new ArrayList<>();
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Bson query = eq("ownerId", userId);
            FindIterable<Document> cursor = collection.find(query);
            for (Document object : cursor){
                List<Track> tracks = convertDocumentPlayListTracks(object);

                Playlist playlist = documentToPlaylist(object);

                playlist.setTracks(tracks);
                playlists.add(playlist);
            }
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
        return playlists;
    }

    public boolean checkPlaylistOwner(String userId, String playlistId){
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Bson byPlaylistId = eq("_id", playlistId);
            Bson byUserID = eq("ownerId", userId);
            FindIterable<Document> cursor = collection.find(byPlaylistId).filter(byUserID);
            if(documentToPlaylist(cursor.first()).getId().isEmpty()){
                return false;
            }
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
        return true;
    }

    public void editPlaylistName(Playlist playlist){
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Playlist newPlaylist = getPlaylist(playlist.getId());
            newPlaylist.setName(playlist.getName());

            collection.findOneAndReplace( playlistToDocument(getPlaylist(playlist.getId())), playlistToDocument(newPlaylist));
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    public void createPlaylist(Playlist playlist){
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            if(playlist.getId() == null){
                playlist.setId(UUID.randomUUID().toString());
            }

            playlist.setTracks(new ArrayList<>());

            collection.insertOne(playlistToDocument(playlist));
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    public void addTrackToPlaylist(String playlistId, Track track){
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Playlist newPlaylist = getPlaylist(playlistId);
            List<Track> newTracks = newPlaylist.getTracks();
            if(newTracks == null){
                newTracks = new ArrayList<>();
            }
            newTracks.add(track);
            newPlaylist.setTracks(newTracks);

            collection.findOneAndReplace(playlistToDocument(getPlaylist(playlistId)), playlistToDocument(newPlaylist));
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    public void removeTrackFromPlaylist(String playlistId, Track removedTrack){
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Playlist newPlaylist = getPlaylist(playlistId);
            List<Track> newTracks = newPlaylist.getTracks();
            for(int i = 0; i < newTracks.size(); i++){
                if(newTracks.get(i).getId() == removedTrack.getId()){
                    newTracks.remove(i);
                }
            }
            newPlaylist.setTracks(newTracks);

            collection.findOneAndReplace(playlistToDocument(getPlaylist(playlistId)), playlistToDocument(newPlaylist));
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    public void deletePlaylist(String playlistId){
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);
            Bson query = eq("_id", playlistId);

            collection.deleteOne(query);
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    private List<Track> convertDocumentPlayListTracks(Document document){
        List<Track> tracks = new ArrayList<>();

        ArrayList<Document> trackDocuments = new ArrayList<>();

        if(document.get("tracks") != null){
            trackDocuments = (ArrayList<Document>) document.get("tracks");

            for(Document documentTrack : trackDocuments){
                Track track = documentToTrack(documentTrack);
                tracks.add(track);
            }
        }
        return tracks;
    }

    private Document playlistToDocument(Playlist playlist){
        return new Document(new BasicDBObject("_id", playlist.getId())
                .append("name", playlist.getName())
                .append("ownerId", playlist.getOwnerId())
                .append("owner", playlist.getOwner())
                .append("tracks", playlist.getTracks()).toMap());
    }

    private Playlist documentToPlaylist(Document cursor){
        return new Playlist(
            (String)cursor.get("_id"),
            (String)cursor.get("name"),
            (String)cursor.get("ownerId"),
            (List<Track>)cursor.get("tracks")
        );
    }

    public Track documentToTrack(Document cursor){
        return new Track(
                (String)cursor.get("_id"),
                (String)cursor.get("title"),
                (String)cursor.get("performer"),
                (String)cursor.get("album"),
                (String)cursor.get("publicationDate"),
                (String)cursor.get("description"),
                (int)cursor.get("duration"),
                (int)cursor.get("playcount"),
                (boolean)cursor.get("offlineAvailable")
        );
    }
}
