package com.tashila.simplecalendar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tashila.simplecalendar.Event;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class EventsDatabase extends RoomDatabase {

    private static EventsDatabase instance;

    public abstract EventsDao eventsDao();

    public static synchronized EventsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), EventsDatabase.class, "events_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
