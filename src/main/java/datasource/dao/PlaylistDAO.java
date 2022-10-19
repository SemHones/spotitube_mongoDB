package datasource.dao;

import datasource.objects.Playlist;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO extends DefaultDAO{

    private static final String SELECT_PLAYLIST_WHERE_ID = "select * from spotitube.playlist where id = ?";
    private static final String SELECT_PLAYLISTS_FROM_USER = "select * from spotitube.playlist where ownerId = ?";
    private static final String SELECT_PLAYLIST_WHERE_ID_AND_OWNER_ID = "select * from spotitube.playlist where id = ? and ownerId = ?";
    private static final String UPDATE_EXISTING_PLAYLIST_NAME = "update spotitube.playlist set name = ? where id = ?";
    private static final String CREATE_NEW_PLAYLIST = "insert into spotitube.playlist (name, owner, ownerId) values (?, ?, ?)";
    private static final String DELETE_PLAYLIST_WHERE_ID = "delete from spotitube.playlist where id = ?";
    private TrackDAO trackDAO = new TrackDAO();

    public PlaylistDAO(){
        super();
    }

    public Playlist getPlaylist(int playlistId){
        Playlist emptyPlaylist = new Playlist(0, "");
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(SELECT_PLAYLIST_WHERE_ID);
            pstmt.setInt(1, playlistId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Playlist item = new Playlist(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("ownerId")
                );
                boolean insidePlaylist = true;
                item.setTracks(trackDAO.getTracks(item.getId(), insidePlaylist));
                return item;
            }
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
        return emptyPlaylist;
    }

    public List<Playlist> getAllPlaylistFromUser(int userId){
        List<Playlist> playlists = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(SELECT_PLAYLISTS_FROM_USER);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Playlist item = new Playlist(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("ownerId")
                );
                boolean insidePlaylist = true;
                item.setTracks(trackDAO.getTracks(item.getId(), insidePlaylist));
                playlists.add(item);
            }
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
        return playlists;
    }

    public boolean checkPlaylistOwner(int userId, int playlistId){
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(SELECT_PLAYLIST_WHERE_ID_AND_OWNER_ID);

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, userId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
        return false;
    }

    public void editPlaylistName(Playlist playlist){
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(UPDATE_EXISTING_PLAYLIST_NAME);

            pstmt.setString(1, playlist.getName());
            pstmt.setInt(2, playlist.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
    }

    public void createPlaylist(Playlist playlist){
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(CREATE_NEW_PLAYLIST);

            pstmt.setString(1, playlist.getName());
            pstmt.setBoolean(2, playlist.getOwner());
            pstmt.setInt(3, playlist.getOwnerId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
    }

    public void deletePlaylist(int playlistId){
        try {
            connection = DriverManager.getConnection(databaseProperties.connectionString());
            pstmt = connection.prepareStatement(DELETE_PLAYLIST_WHERE_ID);

            pstmt.setInt(1, playlistId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        } finally {
            closeConnections();
        }
    }
}
