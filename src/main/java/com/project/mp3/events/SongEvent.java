package com.project.mp3.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SongEvent extends Event {

    public static final EventType<SongEvent> SONG = new EventType<>(Event.ANY, "ANY");
    public static final EventType<SongEvent> ANY = SONG;
    public static final EventType<SongEvent> SONG_CHANGED = new EventType<>(SongEvent.ANY, "SONG_CHANGED");

    public SongEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

}
