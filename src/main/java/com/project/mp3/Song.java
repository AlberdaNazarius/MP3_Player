package com.project.mp3;

import java.io.File;
import java.util.TreeMap;

public class Song {

    private static final TreeMap<File, Integer> addedSongs = new TreeMap<>();

    private static int newId;
    private final int id;
    private final File location;

    private String name;

    public Song(File location) {
        this.location = location;

        if (!isDuplicate(location)){
            this.id = newId++;
            addedSongs.put(location, this.id);
        }
        else
            this.id = addedSongs.get(location);

        String name = location.getName();
        for (int i = 0; i < name.length(); i++){
            if (name.toCharArray()[i] == '.')
                this.name = name.substring(0, i);
        }
    }

    public static boolean isDuplicate(File file){
        return addedSongs.containsKey(file);
    }

    public String getName() {
        return name;
    }

    public int getId(){
        return id;
    }

    public String getURI(){
        return location.toURI().toString();
    }

}
