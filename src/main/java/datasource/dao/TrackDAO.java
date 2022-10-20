package datasource.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import datasource.objects.Track;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class TrackDAO extends DefaultDAO{

    private static final String COLLECTION_NAME = "track";

    private PlaylistDAO playlistDAO = new PlaylistDAO();

    public TrackDAO(){
        super();
    }

    public Track getTrack(String trackId) {
        Track emptyTrack = new Track("", "","","","","", 0, 0, false);
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Bson query = eq("_id", trackId);
            FindIterable<Document> cursor = collection.find(query);
            return documentToTrack(cursor.first());
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
        return emptyTrack;
    }

    public List<Track> getTracks(String playlistId, boolean insidePlaylist) {
        List<Track> tracks = new ArrayList<>();
        try {
            if(insidePlaylist){
                tracks = playlistDAO.getPlaylist(playlistId).getTracks();
            }
            else{
                MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

                FindIterable<Document> cursor = collection.find();
                List<Track> playlistTracks = playlistDAO.getPlaylist(playlistId).getTracks();
                for (Document object : cursor){
                    boolean alreadyExists = false;
                    for(Track track : playlistTracks){
                        if(track.getId() == documentToTrack(object).getId()){
                            alreadyExists = true;
                            break;
                        }
                    }
                    if(!alreadyExists){
                        tracks.add(documentToTrack(object));
                    }
                }
            }
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
        return tracks;
    }


    public void addTrackToPlaylist(String playlistId, String trackId){
        try {
            playlistDAO.addTrackToPlaylist(playlistId, getTrack(trackId));
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    public void setTrackOfflineAvailable(boolean offlineAvailable, String trackId){
        try {
            MongoCollection<Document> collection = openConnection(COLLECTION_NAME);

            Track newTrack = getTrack(trackId);
            newTrack.setOfflineAvailable(offlineAvailable);

            collection.findOneAndReplace( trackToDocument(getTrack(trackId)), trackToDocument(newTrack));
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    public void deleteTrack(String playlistId, String trackId){
        try {
            playlistDAO.removeTrackFromPlaylist(playlistId, getTrack(trackId));
        } catch (MongoException me) {
            logError(me);
        } finally {
            closeConnections();
        }
    }

    public Document trackToDocument(Track track){
        return new Document(new BasicDBObject("_id", track.getId())
                .append("title", track.getTitle())
                .append("performer", track.getPerformer())
                .append("album", track.getAlbum())
                .append("publicationDate", track.getPublicationDate())
                .append("description", track.getDescription())
                .append("duration", track.getDuration())
                .append("playcount", track.getPlaycount())
                .append("offlineAvailable", track.getOfflineAvailable()).toMap());
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
