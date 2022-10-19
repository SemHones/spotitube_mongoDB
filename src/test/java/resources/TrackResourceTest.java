package resources;

import datasource.objects.Track;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import resources.dto.TrackRequestDTO;
import resources.dto.TrackResponseDTO;
import services.TrackService;
import services.UserService;
import services.exceptions.TokenException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

class TrackResourceTest {
    private TrackResource tracksResource;
    private AllTracksResource allTracksResource;
    private TrackResponseDTO trackResponseDTO;
    private TrackRequestDTO trackRequestDTO;

    private UserService userService = mock(UserService.class);
    private TrackService trackService = mock(TrackService.class);

    private static final int PLAYLIST_ID = 1;
    private static final int TRACK_ID = 1;
    private static final boolean TRACK_OFFLINE_AVAILABLE = true;
    private static final String TOKEN = "5431-321";
    private static final List<Track> EMPTY_TRACKS = new ArrayList<>();
    private static final Track EMPTY_TRACK = mock(Track.class);

    @BeforeEach
    void setup() {
        tracksResource = new TrackResource();
        tracksResource.setUserService(userService);
        tracksResource.setTrackService(trackService);

        allTracksResource = new AllTracksResource();
        allTracksResource.setUserService(userService);
        allTracksResource.setTrackService(trackService);

        trackResponseDTO = new TrackResponseDTO();
        trackResponseDTO.setTracks(EMPTY_TRACKS);

        trackRequestDTO = new TrackRequestDTO();
        trackRequestDTO.setId(TRACK_ID);
        trackRequestDTO.setOfflineAvailable(TRACK_OFFLINE_AVAILABLE);
    }

    @Test
    void canGetTrack() {
        // Arrange
        Mockito.when(trackService.getTracksFromPlaylist(PLAYLIST_ID)).thenReturn(trackResponseDTO);

        // Act
        Response response = tracksResource.getTracks(TOKEN, PLAYLIST_ID);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionGetTracks(){
        // Arrange
        Mockito.when(trackService.getTracksFromPlaylist(anyInt())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> tracksResource.getTracks(TOKEN, anyInt()));
    }

    @Test
    void canGetAllTracksOutsidePlaylist() {
        // Arrange
        Mockito.when(trackService.getTracksOutsidePlaylist(PLAYLIST_ID)).thenReturn(trackResponseDTO);

        // Act
        Response response = allTracksResource.getAllTracksOutsidePlaylist(TOKEN, PLAYLIST_ID);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionGetAllTracksOutsidePlaylist(){
        // Arrange
        Mockito.when(trackService.getTracksOutsidePlaylist(anyInt())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> allTracksResource.getAllTracksOutsidePlaylist(TOKEN, anyInt()));
    }

    @Test
    void canAddTrackToPlaylist() {
        // Arrange
        Mockito.when(trackService.addTrackToPlaylist(PLAYLIST_ID, trackRequestDTO)).thenReturn(trackResponseDTO);

        // Act
        Response response = tracksResource.addTrackToPlaylist(TOKEN, PLAYLIST_ID, trackRequestDTO);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionAddTrackToPlaylist(){
        // Arrange
        Mockito.when(trackService.addTrackToPlaylist(anyInt(), any())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> tracksResource.addTrackToPlaylist(TOKEN, anyInt(), trackRequestDTO));
    }

    @Test
    void canRemoveTrackFromPlaylist() {
        // Arrange
        Mockito.when(trackService.removeTrackFromPlaylist(PLAYLIST_ID, TRACK_ID)).thenReturn(trackResponseDTO);

        // Act
        Response response = tracksResource.removeTrackFromPlaylist(TOKEN, PLAYLIST_ID, TRACK_ID);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionRemoveTrackFromPlaylist(){
        // Arrange
        Mockito.when(trackService.removeTrackFromPlaylist(anyInt(), anyInt())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> tracksResource.removeTrackFromPlaylist(TOKEN, PLAYLIST_ID, anyInt()));
    }
}
