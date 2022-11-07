package com.project.mp3.components;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Song implements Serializable {

    private static TreeMap<File, Integer> addedSongs = new TreeMap<>();

    private static int newId;
    private final int id;
    private final File location;

    private String name;
    private String style;

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

    public static TreeMap<File, Integer> getAddedSongs(){
        return addedSongs;
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public static void setAddedSongs(TreeMap<File, Integer> addedSongs) {
        Song.addedSongs = addedSongs;
    }

    private static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
