package com.project.mp3;


import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Arrays;

public class Playlist {

    private static int newId;
    private final int id;

    private String name;
    private ArrayList<Song> songs;

    private static VBox playlistsBox;
    private final JFXButton playlistButton;

    public Playlist(String name) {
        this.id = newId++;
        this.name = name;
        songs = new ArrayList<Song>();

        playlistButton = new JFXButton(name);
        playlistsBox.getChildren().add(playlistButton);
    }

    public void addSong(Song song){
        songs.add(song);
    }

    public void addAllSongs(Song[] songs){
        this.songs.addAll(Arrays.asList(songs));
    }

    public Song getSongByIndex(int index){
        return songs.get(index);
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Button getPlaylistButton() {
        return playlistButton;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static void setPlaylistsBox(VBox playlistsBox) {
        Playlist.playlistsBox = playlistsBox;
    }
}
