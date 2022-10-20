package org.example;

import org.example.datasource.TrackDAO;
import org.example.datasource.util.DatabaseProperties;
import org.example.domain.Track;

import java.util.List;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {
        TrackDAO trackDAO = new TrackDAO();

        String uuid = UUID.randomUUID().toString();
        Track track = new Track(uuid, "Song for someone", "The Frames", 350, "The cost", 1, "9-9-1999", "anything", false);
        String id = "2965038c-5927-41f5-90f6-a68098cef223";
        List<Track> tracks = trackDAO.findAll();

        trackDAO.addTrack(track);
        for(Track tempTrack : tracks){
            System.out.println(tempTrack.toString());
        }
        trackDAO.deleteItem(id);
    }
}