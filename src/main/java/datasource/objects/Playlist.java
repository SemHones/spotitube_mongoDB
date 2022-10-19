package datasource.objects;

import java.util.List;

public class Playlist {
    private String id;
    private String name;
    private String ownerId;
    private boolean owner;
    private List<Track> tracks;

    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
        this.owner = false;
    }

    public Playlist(String id, String name, String ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.owner = true;
    }

    public Playlist(String id, String name, String ownerId, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.owner = true;
        this.tracks = tracks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean getOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
