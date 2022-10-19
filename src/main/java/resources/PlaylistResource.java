package resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import resources.dto.PlaylistRequestDTO;
import resources.dto.PlaylistResponseDTO;
import services.PlaylistService;
import services.UserService;

@Path("/playlists")
public class PlaylistResource {

    private PlaylistService playlistService;
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@QueryParam("token") String token){
        int userID = userService.verifyToken(token);
        PlaylistResponseDTO playlistResponseDTO = playlistService.getPlaylists(userID);
        return Response.ok(playlistResponseDTO).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPlaylist(@QueryParam("token") String token, PlaylistRequestDTO playlist) {
        int userID = userService.verifyToken(token);
        PlaylistResponseDTO playlistResponseDTO = playlistService.createPlaylist(playlist.getPlaylistID(), playlist.getName(), userID);
        return Response.ok(playlistResponseDTO).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response changePlaylistName(@QueryParam("token") String token, @PathParam("id") int playlistID, PlaylistRequestDTO playlist) {
        int userID = userService.verifyToken(token);
        PlaylistResponseDTO playlistResponseDTO = playlistService.editPlaylistName(playlistID, playlist.getName(), userID);
        return Response.ok(playlistResponseDTO).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deletePlaylist(@QueryParam("token") String token, @PathParam("id") int playlistID) {
        int userID = userService.verifyToken(token);
        PlaylistResponseDTO playlistResponseDTO = playlistService.deletePlaylist(playlistID, userID);
        return Response.ok(playlistResponseDTO).build();
    }

    @Inject
    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
