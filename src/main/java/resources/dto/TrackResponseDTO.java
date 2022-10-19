package resources.dto;

import datasource.objects.Track;

import java.util.List;

public class TrackResponseDTO {
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
