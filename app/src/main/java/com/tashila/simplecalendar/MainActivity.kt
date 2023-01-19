package com.tashila.simplecalendar

import android.content.Context
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tashila.simplecalendar.Utils.Companion.getDayAfter
import com.tashila.simplecalendar.database.EventsViewModel
import com.tashila.simplecalendar.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var context: Context
    private lateinit var allEvents: MutableList<Event>
    private lateinit var eventsAdapter: EventsAdapter
    private var selectedDate: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        //variables
        context = this
        eventsAdapter = EventsAdapter()
        allEvents = mutableListOf()
        val recyclerView = binding.recyclerView
        val calendarView = binding.calendarView

        //calendar
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = LocalDate.of(year, month+1, dayOfMonth).dayOfYear
            filterEvents()
        }

        //adapter
        eventsAdapter.onEventClickedListener = {
            ViewEventDialog(it).show(supportFragmentManager, null)
        }

        //recycler view
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }

        //database
        eventsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[EventsViewModel::class.java]
        eventsViewModel.allTasks.observe(this) {
            allEvents.addAll(it)
            if (allEvents.isNotEmpty())
                showList(allEvents)
            else //only for the first time
                addHolidays()
        }

        //views
        binding.newEvent.setOnClickListener {
            val editEventDialog = EditEventDialog(null)
            editEventDialog.onEventAddedListener = { event, isNew ->
                if (isNew)
                    eventsViewModel.insert(event)
                else
                    eventsViewModel.update(event)
            }
            editEventDialog.show(supportFragmentManager, null)
        }
    }

    /**hardcode each type of holiday to the next 3 days after today*/
    private fun addHolidays() {
        val publicHoliday = Event()
        publicHoliday.title = "Public holiday"
        publicHoliday.date = getDayAfter(1)

        val bankHoliday = Event()
        bankHoliday.title = "Bank holiday"
        bankHoliday.date = getDayAfter(2)

        val mercantileHoliday = Event()
        mercantileHoliday.title = "Mercantile holiday"
        mercantileHoliday.date = getDayAfter(3)

        eventsViewModel.insert(publicHoliday)
        eventsViewModel.insert(bankHoliday)
        eventsViewModel.insert(mercantileHoliday)
    }

    private fun filterEvents() {
        val filteredList = mutableListOf<Event>()
        for (event in allEvents) {
            val eventDate = Utils.getLocalDate(event.date).dayOfYear
            if (selectedDate == eventDate)
                filteredList.add(event)
        }

        showList(filteredList)
    }

    private fun showList(list: MutableList<Event>) {
        eventsAdapter.submitList(list)

        if (list.isEmpty())
            binding.noContentLayout.visibility = VISIBLE
        else
            binding.noContentLayout.visibility = INVISIBLE
    }

    companion object {
        const val TAG = "MainActivity"
    }
}