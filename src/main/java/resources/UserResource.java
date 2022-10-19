package resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import resources.dto.LoginRequestDTO;
import resources.dto.LoginResponseDTO;
import services.UserService;

@Path("/login")
public class UserResource {

    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponse = userService.login(loginRequestDTO.getUser(), loginRequestDTO.getPassword());

        if (loginResponse.getUser().isEmpty()) {
            return Response.status(401).build();
        }
        return Response.ok(loginResponse).build();
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
