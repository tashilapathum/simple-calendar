package com.tashila.simplecalendar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tashila.simplecalendar.Utils.Companion.getDate
import com.tashila.simplecalendar.Utils.Companion.getTime
import com.tashila.simplecalendar.Utils.Companion.isValid
import com.tashila.simplecalendar.databinding.DialogEditEventBinding

class EditEventDialog(private val editingEvent: Event?) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogEditEventBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var event: Event
    private var isNew: Boolean = false
    var onEventAddedListener: ((Event, Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // to resize the whole screen when soft keyboard is visible
        getDialog()!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditEventBinding.inflate(layoutInflater)

        dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)

        init()
        return dialog
    }

    private fun init() {
        if (editingEvent != null) {
            event = editingEvent
            binding.dialogTitle.text = "Edit Event"
            fillData()
        } else {
            event = Event()
            isNew = true
        }

        //set date picker
        binding.date.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(System.currentTimeMillis()).build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                event.date = selection
                val date = getDate(selection)
                binding.date.editText?.setText(date)
            }
            datePicker.show(childFragmentManager, "date picker")
        }

        //set starting and ending time pickers
        binding.start.editText?.setOnClickListener {
            val timePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                event.startTime = Utils.getTimeInMillis(timePicker)
                val time = getTime(timePicker)
                binding.start.editText?.setText(time)
            }
            timePicker.show(childFragmentManager, "time picker")
        }

        binding.end.editText?.setOnClickListener {
            val timePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                event.endTime = Utils.getTimeInMillis(timePicker)
                val time = getTime(timePicker)
                binding.end.editText?.setText(time)
            }
            timePicker.show(childFragmentManager, "time picker")
        }

        //add or cancel
        binding.cancel.setOnClickListener { dialog.dismiss() }
        binding.addTask.setOnClickListener {
            //if one of starting and ending time is added, the other is required as well.
            if (isValid(binding.start) || isValid(binding.end)) {
                if (isValid(binding.title, binding.date, binding.start, binding.end))
                    save(isNew)
            } //otherwise it's considered as an all day event
            else if (isValid(binding.title, binding.date))
                save(isNew)
        }
    }

    private fun fillData() {
        if (editingEvent != null)
            binding.apply {
                title.editText?.setText(editingEvent.title)
                date.editText?.setText(getDate(editingEvent.date))
                if (editingEvent.startTime != 0L && editingEvent.endTime != 0L) {
                    start.editText?.setText(getTime(editingEvent.startTime))
                    end.editText?.setText(getTime(editingEvent.endTime))
                }
                note.editText?.setText(editingEvent.note)
            }
    }

    private fun save(isNew: Boolean) {
        //title and note
        event.title = binding.title.editText?.text.toString().trim()
        event.note = binding.note.editText?.text.toString().trim()

        //reminder time is stored as just a string because actual notifications aren't implemented
        val checkedChip = binding.reminderGroup.checkedChipId
        var reminder = "None"
        when (checkedChip) {
            0 -> reminder = "15 mins"
            1 -> reminder = "1 hour"
            2 -> reminder = "1 day"
        }
        event.reminder = reminder

        //add
        onEventAddedListener?.invoke(event, isNew)
        dialog.dismiss()
    }
}