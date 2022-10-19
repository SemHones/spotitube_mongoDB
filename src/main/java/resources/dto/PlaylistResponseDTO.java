package resources.dto;

import datasource.objects.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistResponseDTO {

    private List<Playlist> playlists = new ArrayList<Playlist>();
    private int length;

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
