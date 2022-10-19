package services;

import datasource.dao.PlaylistDAO;
import datasource.objects.Playlist;
import datasource.objects.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import resources.dto.PlaylistResponseDTO;
import services.exceptions.TokenException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class PlaylistServiceTest {


    private PlaylistService playlistService;
    private PlaylistResponseDTO playlistResponseDTO;

    private PlaylistDAO playlistDAO = mock(PlaylistDAO.class);

    private static final int USER_ID = 1;
    private static final int EMPTY_PLAYLIST_LENGTH = 0;
    private static final int FILLED_PLAYLIST_LENGTH = 400;
    private static final List<Playlist> EMPTY_PLAYLISTS = new ArrayList<>();

    @BeforeEach
    void setup() {
        playlistService = new PlaylistService();
        playlistService.setPlaylistDAO(playlistDAO);

        playlistResponseDTO = new PlaylistResponseDTO();
        playlistResponseDTO.setPlaylists(EMPTY_PLAYLISTS);

    }

    @Test
    void canGetPlaylists() {
        // Arrange
        Mockito.when(playlistDAO.getAllPlaylistFromUser(USER_ID)).thenReturn(EMPTY_PLAYLISTS);

        // Act
        PlaylistResponseDTO playlistsResponseDTO = playlistService.getPlaylists(USER_ID);

        // Assert
        assertNotNull(playlistsResponseDTO);
    }

    @Test
    void getLengthOfEmptyPlaylist() {
        // Arrange
        Mockito.when(playlistDAO.getAllPlaylistFromUser(USER_ID)).thenReturn(EMPTY_PLAYLISTS);

        // Act
        int length = playlistService.calculateDurationOfAllPlaylist(EMPTY_PLAYLISTS);

        // Assert
        assertEquals(EMPTY_PLAYLIST_LENGTH, length);
    }

    @Test
    void getLengthOfFilledPlaylist() {
        // Arrange
        List<Playlist> filledPlaylists = new ArrayList<>();
        Playlist playlist = new Playlist(1, "metal", 1);

        List<Track> tracks = new ArrayList<>();
        Track track = new Track(1, "","", "","","",FILLED_PLAYLIST_LENGTH,0,false);
        tracks.add(track);

        playlist.setTracks(tracks);
        filledPlaylists.add(playlist);

        Mockito.when(playlistDAO.getAllPlaylistFromUser(USER_ID)).thenReturn(filledPlaylists);

        // Act
        PlaylistResponseDTO playlistsResponseDTO = playlistService.getPlaylists(USER_ID);

        // Assert
        assertEquals(FILLED_PLAYLIST_LENGTH, playlistsResponseDTO.getLength());
    }
}
