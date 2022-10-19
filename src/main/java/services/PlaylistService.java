package services;

import datasource.dao.PlaylistDAO;
import datasource.objects.Playlist;
import datasource.objects.Track;
import jakarta.inject.Inject;
import resources.dto.PlaylistResponseDTO;
import services.exceptions.UnauthorizedException;

import java.util.List;

public class PlaylistService {

    private PlaylistDAO playlistDAO;

    public PlaylistResponseDTO getPlaylists(int userId){
        PlaylistResponseDTO playlistResponseDTO = new PlaylistResponseDTO();

        playlistResponseDTO.setPlaylists(playlistDAO.getAllPlaylistFromUser(userId));
        playlistResponseDTO.setLength(calculateDurationOfAllPlaylist(playlistResponseDTO.getPlaylists()));
        return playlistResponseDTO;
    }

    public int calculateDurationOfAllPlaylist(List<Playlist> playlists){
        int totalDuration = 0;
        for (Playlist playlist : playlists){
            List<Track> tracks = playlist.getTracks();
            for (Track track : tracks){
                totalDuration += track.getDuration();
            }
        }
        return totalDuration;
    }

    public PlaylistResponseDTO editPlaylistName(int playlistId, String name, int userId) throws UnauthorizedException {
        Playlist playlistObject = new Playlist(playlistId, name, userId);

        if(playlistDAO.checkPlaylistOwner(userId, playlistId)){
            playlistDAO.editPlaylistName(playlistObject);
            return getPlaylists(userId);
        }
        throw new UnauthorizedException();
    }

    public PlaylistResponseDTO createPlaylist(int playlistId, String name, int userId){
        Playlist playlistObject = new Playlist(playlistId, name, userId);

        playlistDAO.createPlaylist(playlistObject);
        return getPlaylists(userId);
    }

    public PlaylistResponseDTO deletePlaylist(int playlistId, int userId) throws UnauthorizedException{
        if(playlistDAO.checkPlaylistOwner(userId, playlistId)){
            playlistDAO.deletePlaylist(playlistId);
            return getPlaylists(userId);
        }
        throw new UnauthorizedException();
    }

    @Inject
    public void setPlaylistDAO(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }
}
