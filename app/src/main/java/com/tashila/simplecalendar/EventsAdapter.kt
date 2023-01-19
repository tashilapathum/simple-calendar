package com.tashila.simplecalendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tashila.simplecalendar.databinding.SampleEventBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class EventsAdapter : ListAdapter<Event, EventsAdapter.EventViewHolder>(DIFF_CALLBACK) {
    var onEventClickedListener: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = SampleEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(private val binding: SampleEventBinding) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: Event) {
            val currentEvent: Event = getItem(adapterPosition)
            val localDate: LocalDate = Utils.getLocalDate(event.date)

            binding.apply {
                dayName.text = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                dayNumber.text = localDate.dayOfMonth.toString()
                date.text = Utils.getDate(event.date)
                title.text = currentEvent.title
                if (event.startTime != 0L && event.endTime != 0L)
                    time.text = "From ${Utils.getTime(event.startTime)} to ${Utils.getTime(event.endTime)}"
                else
                    time.text = "All day"
                eventCard.setOnClickListener { onEventClickedListener?.invoke(event) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: ItemCallback<Event> = object : ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }

        }
    }

}