package com.tashila.simplecalendar

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.get
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tashila.simplecalendar.Utils.Companion.getTime
import com.tashila.simplecalendar.databinding.DialogEditEventBinding
import com.tashila.simplecalendar.databinding.DialogViewEventBinding

class ViewEventDialog(private val event: Event) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogViewEventBinding
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogViewEventBinding.inflate(layoutInflater)

        dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)

        init()
        return dialog
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        binding.apply {
            title.text = event.title
            date.text = Utils.getDate(event.date)
            if (event.startTime != 0L && event.endTime != 0L)
                time.text = "From ${getTime(event.startTime)} to ${getTime(event.endTime)}"
            else
                time.text = "All day"
            if (event.note.isNotEmpty())
                note.text = event.note
            else
                note.visibility = GONE
            edit.setOnClickListener { EditEventDialog(event).show(childFragmentManager, null) }
            close.setOnClickListener { dialog.dismiss() }
        }
    }

}