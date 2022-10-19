package services;

import datasource.dao.UserDAO;
import datasource.objects.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import resources.dto.LoginResponseDTO;
import services.exceptions.TokenException;
import services.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

class UserServiceTest {
    private UserService userService;

    private final UserDAO userDAO = mock(UserDAO.class);
    private User user;

    private static final String USERNAME ="thomas";
    private static final String PASSWORD ="543";
    private static final String TOKEN = "5431-321";
    private static final int USERID = 1;

    @BeforeEach
    void setup() {
        userService = new UserService();
        userService.setUserDAO(userDAO);

        user = new User(USERID, USERNAME, TOKEN, PASSWORD);
    }

    @Test
    void canGetUserByValidToken() {
        // Arrange
        Mockito.when(userDAO.getSingleUserByToken(TOKEN)).thenReturn(user);

        // Act
        int userID = userService.verifyToken(TOKEN);

        // Assert
        assertEquals(userID, user.getId());
    }

    @Test
    void canNotVerifyToken() {
        // Arrange
        Mockito.when(userDAO.getSingleUserByToken(anyString())).thenThrow(TokenException.class);

        // Act

        // Assert
        assertThrows(TokenException.class, () -> userService.verifyToken(anyString()));
    }

    @Test
    void canLogin() {
        // Arrange
        Mockito.when(userDAO.getSingleUser(USERNAME, PASSWORD)).thenReturn(user);

        // Act
        LoginResponseDTO loginResponseDTO = userService.login(USERNAME, PASSWORD);

        // Assert
        assertEquals(loginResponseDTO.getToken(), user.getToken());
    }

    @Test
    void canNotLogin() {
        // Arrange
        Mockito.when(userDAO.getSingleUser(anyString(), anyString())).thenThrow(UnauthorizedException.class);

        // Act

        // Assert
        assertThrows(UnauthorizedException.class, () -> userService.login(anyString(), anyString()));
    }
}
