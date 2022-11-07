package com.project.mp3;

import com.project.mp3.components.Playlist;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class Data implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<Playlist> savedPlaylists;
    private TreeMap<File, Integer> addedSongs;
    private int songIndex;
    private  int selectedPlaylist;


    public Data() {
    }

    public Data(ArrayList<Playlist> savedPlaylists, int selectedPlaylist, TreeMap<File, Integer> addedSongs, int songIndex) {
        this.savedPlaylists = savedPlaylists;
        this.addedSongs = addedSongs;
        this.selectedPlaylist = selectedPlaylist;
        this.songIndex = songIndex;
    }

    // Getters
    public ArrayList<Playlist> getSavedPlaylists() {
        return savedPlaylists;
    }
    public TreeMap<File, Integer> getAddedSongs() {
        return addedSongs;
    }

    public int getSongIndex() {
        return songIndex;
    }
    public int getSelectedPlaylist() {
        return selectedPlaylist;
    }
}
