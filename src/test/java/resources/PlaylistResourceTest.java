package resources;

import datasource.objects.Playlist;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import resources.dto.PlaylistRequestDTO;
import resources.dto.PlaylistResponseDTO;
import services.PlaylistService;
import services.UserService;
import services.exceptions.TokenException;
import services.exceptions.UnauthorizedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PlaylistResourceTest {


    private PlaylistResource playlistResource;
    private PlaylistRequestDTO playlistRequestDTO;
    private PlaylistResponseDTO playlistResponseDTO;

    private UserService userService = mock(UserService.class);
    private PlaylistService playlistService = mock(PlaylistService.class);

    private static final String TOKEN = "5431-321";
    private static final int USER_ID = 1;
    private static final int PLAYLIST_ID = 1;
    private static final String PLAYLIST_NAME = "Metal";
    private static final boolean OWNER = false;
    private static final int LENGTH = 1;

    private List<Playlist> playlists = new ArrayList();

    @BeforeEach
    void setup() {
        playlistResource = new PlaylistResource();

        playlistResource.setUserService(userService);
        playlistResource.setPlaylistService(playlistService);

        playlistRequestDTO = new PlaylistRequestDTO();
        playlistRequestDTO.setPlaylistID(PLAYLIST_ID);
        playlistRequestDTO.setName(PLAYLIST_NAME);
        playlistRequestDTO.setOwner(OWNER);

        playlistResponseDTO = new PlaylistResponseDTO();
        playlistResponseDTO.setPlaylists(playlists);
        playlistResponseDTO.setLength(LENGTH);
    }

    @Test
    void checkIfTokenIsTheSame() {
        // Arrange
        Mockito.when(userService.verifyToken(TOKEN)).thenReturn(USER_ID);

        // Act
        Response response = playlistResource.getPlaylists(TOKEN);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void checkIfTokenIsNotTheSame() {
        // Arrange
        Mockito.when(userService.verifyToken(anyString())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> playlistResource.getPlaylists(anyString()));
    }

    @Test
    void canGetPlaylists() {
        // Arrange
        Mockito.when(playlistService.getPlaylists(USER_ID)).thenReturn(playlistResponseDTO);

        // Act
        Response response = playlistResource.getPlaylists(TOKEN);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionGetPlaylists() {
        // Arrange
        Mockito.when(playlistService.getPlaylists(anyInt())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> playlistResource.getPlaylists(anyString()));
    }

    @Test
    void canCreatePlaylist() {
        // Arrange
        Mockito.when(playlistService.createPlaylist(playlistRequestDTO.getPlaylistID(), playlistRequestDTO.getName(), USER_ID)).thenReturn(playlistResponseDTO);

        // Act
        Response response = playlistResource.createPlaylist(TOKEN, playlistRequestDTO);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionCreatePlaylist() {
        // Arrange
        Mockito.when(playlistService.createPlaylist(anyInt(), anyString(), anyInt())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> playlistResource.createPlaylist(anyString(), playlistRequestDTO));
    }

    @Test
    void canChangePlaylistName() {
        // Arrange
        Mockito.when(playlistService.editPlaylistName(PLAYLIST_ID, PLAYLIST_NAME, USER_ID)).thenReturn(playlistResponseDTO);

        // Act
        Response response = playlistResource.changePlaylistName(TOKEN, PLAYLIST_ID, playlistRequestDTO);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionChangePlaylistName() {
        // Arrange
        Mockito.when(playlistService.editPlaylistName(anyInt(), anyString(), anyInt())).thenThrow(UnauthorizedException.class);

        // Act

        // Assert
        assertThrows(UnauthorizedException.class, () -> playlistResource.changePlaylistName(TOKEN, anyInt(), playlistRequestDTO));
    }

    @Test
    void canDeletePlaylist() {
        // Arrange
        Mockito.when(playlistService.deletePlaylist(PLAYLIST_ID, USER_ID)).thenReturn(playlistResponseDTO);

        // Act
        Response response = playlistResource.deletePlaylist(TOKEN, PLAYLIST_ID);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void throwExceptionDeletePlaylist() {
        // Arrange
        Mockito.when(playlistService.deletePlaylist(anyInt(), anyInt())).thenThrow(UnauthorizedException.class);

        // Act

        // Assert
        assertThrows(UnauthorizedException.class, () -> playlistResource.deletePlaylist(TOKEN, anyInt()));
    }


}
