package com.tashila.simplecalendar.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tashila.simplecalendar.Event;

import java.util.List;

public class EventsViewModel extends AndroidViewModel {
    private final EventsRepository repository;

    public EventsViewModel(@NonNull Application application) {
        super(application);
        repository = new EventsRepository(application);
    }

    public void insert(Event event) {
        repository.insert(event);
    }

    public void update(Event event) {
        repository.update(event);
    }

    public void delete(Event event) {
        repository.delete(event);
    }

    public void deleteAllTasks() {
        repository.deleteAll();
    }

    public LiveData<List<Event>> getAllTasks() {
        return repository.getAllEvents();
    }
}
