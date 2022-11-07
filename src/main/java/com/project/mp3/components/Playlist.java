package com.project.mp3.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.project.mp3.MP3_Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Playlist implements Serializable {

    private transient static int  newId;
    private transient final int id;

    private String name;
    private ArrayList<Song> songs;

    private static VBox playlistsBox;
    private static JFXListView<Song> songsListView;
    private final JFXButton playlistButton;


    public Playlist(String name) {
        this.id = newId++;
        this.name = name;
        songs = new ArrayList<Song>();

        playlistButton = new JFXButton(name);
        setDefaultButtonStyle();
        playlistsBox.getChildren().add(playlistButton); // add button to VBox component
        playlistButton.onActionProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (MP3_Controller.selectedPlaylist == id)
                    return;

                var previousPlaylist = MP3_Controller.playlists.stream().filter(playlist -> playlist.getId() == MP3_Controller.selectedPlaylist).toList().get(0);
                previousPlaylist.playlistButton.setStyle(getDefaultButtonStyle());

                MP3_Controller.selectedPlaylist = id;
                playlistButton.setStyle("-fx-text-fill: #34B743; -fx-font-weight: bold;");
                // refreshes songsListView
                songsListView.getItems().clear();
                for (var song : songs)
                    songsListView.getItems().add(song);
            }
        });
    }

    public Playlist(String name, ArrayList<Song> songs){
        this.id = newId++;
        this.name = name;
        this.songs = songs;

        playlistButton = new JFXButton(name);
        setDefaultButtonStyle();
        playlistsBox.getChildren().add(playlistButton); // add button to VBox component
        playlistButton.onActionProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (MP3_Controller.selectedPlaylist == id)
                    return;

                var previousPlaylist = MP3_Controller.playlists.stream().filter(playlist -> playlist.getId() == MP3_Controller.selectedPlaylist).toList().get(0);
                previousPlaylist.playlistButton.setStyle(getDefaultButtonStyle());

                MP3_Controller.selectedPlaylist = id;
                playlistButton.setStyle("-fx-text-fill: #34B743; -fx-font-weight: bold;");
                // refreshes songsListView
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

    public void removeSongByIndex(int index){
        songs.remove(index);
    }

    public Song getSongByIndex(int index){
        return songs.get(index);
    }
    public ArrayList<Song> getAllSongs(){
        return songs;
    }

    // Realize serializable methods
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(name);
        oos.writeObject(songs);
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        name = ois.readUTF();
        songs = (ArrayList<Song>) ois.readObject();
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
    public static String getDefaultButtonStyle(){
        return "-fx-text-fill: white; -fx-font-weight: bold;";
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
    public void setDefaultButtonStyle(){
        playlistButton.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
    }
}
