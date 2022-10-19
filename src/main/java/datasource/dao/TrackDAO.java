package datasource.dao;

import datasource.objects.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackDAO extends DefaultDAO{

    private static final String SELECT_TRACKS_WHERE_ID = "select * from spotitube.track where id = ?";
    private static final String SELECT_TRACKS_FROM_PLAYLIST = "select t.* from spotitube.track t inner join spotitube.track_on_playlist tp on t.id = tp.trackId where tp.playlistId = ?";
    private static final String SELECT_TRACKS_OUTSIDE_PLAYLIST = "select * from spotitube.track where id not in (select t.id from spotitube.track t inner join spotitube.track_on_playlist tp on t.id = tp.trackId where tp.playlistId = ?)";
    private static final String ADD_TRACK_TO_PLAYLIST = "insert into spotitube.track_on_playlist (playlistId, trackId) values (?, ?)";
    private static final String UPDATE_TRACK_OFFLINE_AVAILABLE = "update spotitube.track set offlineAvailable = ? where id = ?";
    private static final String DELETE_TRACK_FROM_PLAYLIST = "delete from spotitube.track_on_playlist where playlistId = ? and trackId = ?";

    public TrackDAO(){
        super();
    }

    public Track getTrack(int trackId) {
        Track emptyTrack = new Track(0, "","","","","", 0, 0, false);
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(SELECT_TRACKS_WHERE_ID);

            pstmt.setInt(1, trackId);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                return new Track(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("performer"),
                    rs.getString("album"),
                    rs.getString("publicationDate"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getInt("playCount"),
                    rs.getBoolean("offlineAvailable")
                );
            }
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
        return emptyTrack;
    }

    public List<Track> getTracks(int playlistId, boolean insidePlaylist) {
        List<Track> tracks = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            if(insidePlaylist){
                pstmt = connection.prepareStatement(SELECT_TRACKS_FROM_PLAYLIST);
            }
            else{
                pstmt = connection.prepareStatement(SELECT_TRACKS_OUTSIDE_PLAYLIST);
            }

            pstmt.setInt(1, playlistId);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Track item = new Track(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("performer"),
                    rs.getString("album"),
                    rs.getString("publicationDate"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getInt("playCount"),
                    rs.getBoolean("offlineAvailable")
                );
                tracks.add(item);
            }
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
        return tracks;
    }


    public void addTrackToPlaylist(int playlistId, int trackId){
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(ADD_TRACK_TO_PLAYLIST);

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, trackId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
    }

    public void setTrackOfflineAvailable(boolean offlineAvailable, int trackId){
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(UPDATE_TRACK_OFFLINE_AVAILABLE);

            pstmt.setBoolean(1, offlineAvailable);
            pstmt.setInt(2, trackId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
    }

    public void deleteTrack(int playlistId, int trackId){
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(DELETE_TRACK_FROM_PLAYLIST);

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, trackId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
    }
}
