package services;

import datasource.dao.TrackDAO;
import datasource.objects.Track;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import resources.dto.TrackRequestDTO;
import resources.dto.TrackResponseDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

class TrackServiceTest {
    private TrackService trackService;
    private TrackResponseDTO tracksResponseDTO;

    private TrackDAO trackDAO = mock(TrackDAO.class);

    private TrackRequestDTO trackRequestDTO;
    private static final int PLAYLIST_ID = 1;
    private static final int TRACK_ID = 1;
    private static final String TRACK_TITLE = "Roman sky";
    private static final boolean TRACK_OFFLINE_AVAILABLE = true;
    private static final List<Track> EMPTY_TRACKS = new ArrayList<>();
    private static final boolean INSIDE_PLAYLIST = true;
    private static final boolean OUTSIDE_PLAYLIST = true;

    @BeforeEach
    void setup(){
        trackService = new TrackService();
        trackService.setTrackDAO(trackDAO);

        tracksResponseDTO = new TrackResponseDTO();
        tracksResponseDTO.setTracks(EMPTY_TRACKS);

        trackRequestDTO = new TrackRequestDTO();
        trackRequestDTO.setId(TRACK_ID);
        trackRequestDTO.setOfflineAvailable(TRACK_OFFLINE_AVAILABLE);
    }

    @Test
    void canGetTracksFromPlaylist(){
        // Arrange
        Mockito.when(trackDAO.getTracks(PLAYLIST_ID, INSIDE_PLAYLIST)).thenReturn(EMPTY_TRACKS);

        // Act
        TrackResponseDTO tracksResponseDTO = trackService.getTracksFromPlaylist(PLAYLIST_ID);

        // Assert
        assertNotNull(tracksResponseDTO);
    }

    @Test
    void canGetTracksOutsidePlaylist(){
        // Arrange
        Mockito.when(trackDAO.getTracks(PLAYLIST_ID, OUTSIDE_PLAYLIST)).thenReturn(EMPTY_TRACKS);

        // Act
        TrackResponseDTO tracksResponseDTO = trackService.getTracksOutsidePlaylist(PLAYLIST_ID);

        // Assert
        assertNotNull(tracksResponseDTO);
    }

    @Test
    void canAddTrackToPlaylist(){
        // Arrange
        Track trackWithTitle = new Track(TRACK_ID, TRACK_TITLE, "", "", "", "", 0, 0, TRACK_OFFLINE_AVAILABLE);
        Mockito.when(trackDAO.getTrack(TRACK_ID)).thenReturn(trackWithTitle);

        // Act
        TrackResponseDTO tracksResponseDTO = trackService.addTrackToPlaylist(PLAYLIST_ID, trackRequestDTO);

        // Assert
        assertNotNull(tracksResponseDTO.getTracks());
    }

    @Test
    void throwExceptionAddTrackToPlaylist(){
        // Arrange
        Mockito.when(trackDAO.getTrack(anyInt())).thenThrow(NotFoundException.class);

        // Act

        // Assert
        assertThrows(NotFoundException.class, () -> trackService.addTrackToPlaylist(anyInt(), trackRequestDTO));
    }

    @Test
    void canRemoveTrackFromPlaylist(){
        // Arrange
        Track trackWithTitle = new Track(TRACK_ID, TRACK_TITLE, "", "", "", "", 0, 0, TRACK_OFFLINE_AVAILABLE);
        Mockito.when(trackDAO.getTrack(TRACK_ID)).thenReturn(trackWithTitle);

        // Act
        TrackResponseDTO tracksResponseDTO = trackService.removeTrackFromPlaylist(PLAYLIST_ID, TRACK_ID);

        // Assert
        assertNotNull(tracksResponseDTO.getTracks());
    }

    @Test
    void throwExceptionRemoveTrackFromPlaylist(){
        // Arrange
        Mockito.when(trackDAO.getTrack(anyInt())).thenThrow(NotFoundException.class);

        // Act

        // Assert
        assertThrows(NotFoundException.class, () -> trackService.removeTrackFromPlaylist(PLAYLIST_ID, anyInt()));
    }
}
