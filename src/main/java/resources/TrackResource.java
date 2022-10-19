package resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import resources.dto.TrackRequestDTO;
import resources.dto.TrackResponseDTO;
import services.TrackService;
import services.UserService;

@Path("playlists/{playlistId}/tracks")
public class TrackResource {
    private TrackService trackService;
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@QueryParam("token") String token, @PathParam("playlistId") int playlistID) {
        userService.verifyToken(token);
        TrackResponseDTO tracksResponseDTO = trackService.getTracksFromPlaylist(playlistID);
        return Response.ok(tracksResponseDTO).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@QueryParam("token") String token, @PathParam("playlistId") int playlistID, TrackRequestDTO track) {
        userService.verifyToken(token);
        TrackResponseDTO tracksResponseDTO = trackService.addTrackToPlaylist(playlistID, track);
        return Response.ok(tracksResponseDTO).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response removeTrackFromPlaylist(@QueryParam("token") String token, @PathParam("playlistId") int playlistID, @PathParam("id") int trackId) {
        userService.verifyToken(token);
        TrackResponseDTO tracksResponseDTO = trackService.removeTrackFromPlaylist(playlistID, trackId);
        return Response.ok(tracksResponseDTO).build();
    }

    @Inject
    public void setTrackService(TrackService trackService) {
        this.trackService = trackService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
