package com.project.mp3;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private static JFXListView<Song> songsListView;
    //private static
    private final JFXButton playlistButton;


    public Playlist(String name) {
        this.id = newId++;
        this.name = name;
        songs = new ArrayList<Song>();

        playlistButton = new JFXButton(name);
        playlistsBox.getChildren().add(playlistButton); // add button to VBox component
        playlistButton.onActionProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (MP3_Controller.selectedPlaylist == id)
                    return;

                var previousPlaylist = MP3_Controller.playlists.stream().filter(playlist -> playlist.getId() == MP3_Controller.selectedPlaylist).toList().get(0);
                previousPlaylist.playlistButton.setStyle("-fx-text-fill: black");

                MP3_Controller.selectedPlaylist = id;
                playlistButton.setStyle("-fx-text-fill: green");

                songsListView.getItems().clear();
                for (var song : songs)
                    songsListView.getItems().add(song);
            }
        });
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
    public ArrayList<Song> getAllSongs(){
        return songs;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Button getPlaylistButton() {
        return playlistButton;
    }
    public int getSize(){
        return songs.size();
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public static void setPlaylistsBox(VBox playlistsBox) {
        Playlist.playlistsBox = playlistsBox;
    }
    public static void setSongsListView(JFXListView<Song> songsListView) {
        Playlist.songsListView = songsListView;
    }
}
