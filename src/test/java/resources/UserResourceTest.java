package resources;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import resources.dto.LoginResponseDTO;
import resources.dto.LoginRequestDTO;
import services.UserService;
import services.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserResourceTest {

    private UserResource userResource;
    private LoginRequestDTO loginRequestDTO;
    private LoginResponseDTO loginResponseDTO;

    private final UserService userService = mock(UserService.class);

    private static final String USERNAME ="thomas";
    private static final String PASSWORD ="543";
    private static final String TOKEN = "5431-321";

    @BeforeEach
    void setup() {
        userResource = new UserResource();
        userResource.setUserService(userService);

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUser(USERNAME);
        loginRequestDTO.setUser(PASSWORD);

        loginResponseDTO = new LoginResponseDTO(USERNAME, TOKEN);
    }

    @Test
    void loginCredentialsAreOkTest() throws UnauthorizedException {
        // Arrange
        Mockito.when(userService.login(loginRequestDTO.getUser(), loginRequestDTO.getPassword())).thenReturn(loginResponseDTO);

        // Act
        Response response = userResource.login(loginRequestDTO);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void loginCredentialsAreNotOkTest() throws UnauthorizedException {
        // Arrange
        LoginRequestDTO emptyLoginRequestDTO = new LoginRequestDTO();
        emptyLoginRequestDTO.setUser("");
        emptyLoginRequestDTO.setPassword("");

        Mockito.when(userService.login(anyString(), anyString())).thenThrow(UnauthorizedException.class);

        // Act

        // Assert
        assertThrows(UnauthorizedException.class, () -> userResource.login(emptyLoginRequestDTO));
    }
}
