package com.tashila.simplecalendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events_table")
data class Event (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var note: String = "",
    var reminder: String = "",
    var date: Long = 0,
    var startTime: Long = 0L,
    var endTime: Long = 0L,
)