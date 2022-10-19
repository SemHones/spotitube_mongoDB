package services;

import datasource.dao.UserDAO;
import datasource.objects.User;
import jakarta.inject.Inject;
import resources.dto.LoginResponseDTO;
import services.exceptions.TokenException;

public class UserService {

    private UserDAO userDAO;

    public int verifyToken(String token) throws TokenException {
        User user = userDAO.getSingleUserByToken(token);
        if(!user.getUsername().isEmpty()){
            return user.getId();
        }
        throw new TokenException();
    }

    public LoginResponseDTO login(String username, String password){
        User user = userDAO.getSingleUser(username, password);
        if (!user.getUsername().isEmpty()){
            return new LoginResponseDTO(user.getUsername(), user.getToken());
        }
        return new LoginResponseDTO("", "");
    }

    @Inject
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
