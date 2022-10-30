package com.project.mp3;

import java.io.File;

public class Song {

    private static int newId;

    private final int id;
    private final File location;

    private String name;

    public Song(File location) {
        this.id = newId++;
        this.location = location;

        String name = location.getName();
        for (int i = 0; i < name.length(); i++){
            if (name.toCharArray()[i] == '.')
                this.name = name.substring(0, i);
        }
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
