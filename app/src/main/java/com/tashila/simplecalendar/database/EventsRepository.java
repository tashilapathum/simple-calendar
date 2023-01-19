package com.tashila.simplecalendar.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tashila.simplecalendar.Event;

import java.util.List;

public class EventsRepository {
    private EventsDao eventsDao;
    private LiveData<List<Event>> allEvents;

    public EventsRepository(Application application) {
        EventsDatabase database = EventsDatabase.getInstance(application);
        eventsDao = database.eventsDao();
        allEvents = eventsDao.getAllEvents();
    }

    public void insert(Event event) {
        new InsertEventAsyncTask(eventsDao).execute(event);
    }

    public void update(Event event) {
        new UpdateEventAsyncTask(eventsDao).execute(event);
    }

    public void delete(Event event) {
        new DeleteEventAsyncTask(eventsDao).execute(event);
    }

    public void deleteAll() {
        new DeleteAllEventsAsyncTask(eventsDao).execute();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventsDao eventsDao;

        private InsertEventAsyncTask(EventsDao eventsDao) {
            this.eventsDao = eventsDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            eventsDao.insert(events[0]);
            return null;
        }
    }

    private static class UpdateEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventsDao eventsDao;

        private UpdateEventAsyncTask(EventsDao eventsDao) {
            this.eventsDao = eventsDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            eventsDao.update(events[0]);
            return null;
        }
    }

    private static class DeleteEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventsDao eventsDao;

        private DeleteEventAsyncTask(EventsDao eventsDao) {
            this.eventsDao = eventsDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            eventsDao.delete(events[0]);
            return null;
        }
    }

    private static class DeleteAllEventsAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventsDao eventsDao;

        private DeleteAllEventsAsyncTask(EventsDao eventsDao) {
            this.eventsDao = eventsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventsDao.deleteAllEvents();
            return null;
        }
    }
}
