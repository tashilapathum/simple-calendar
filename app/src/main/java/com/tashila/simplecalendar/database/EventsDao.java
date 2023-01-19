package com.tashila.simplecalendar.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tashila.simplecalendar.Event;

import java.util.List;

@Dao
public interface EventsDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM events_table")
    void deleteAllEvents();

    @Query("SELECT * FROM events_table ORDER BY startTime ASC")
    LiveData<List<Event>> getAllEvents();
}
