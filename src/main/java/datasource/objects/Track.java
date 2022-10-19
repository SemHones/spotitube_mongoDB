package datasource.objects;

public class Track {
    private int id;
    private String title;
    private String performer;
    private String album;
    private String publicationDate;
    private String description;
    private int duration;
    private int playcount;
    private boolean offlineAvailable;

    public Track(int id, String title, String performer, String album, String publicationDate, String description, int duration, int playcount, boolean offlineAvailable) {
        this.id = id;
        this.title = title;
        this.performer = performer;
        this.album = album;
        this.publicationDate = publicationDate;
        this.description = description;
        this.duration = duration;
        this.playcount = playcount;
        this.offlineAvailable = offlineAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }
}
