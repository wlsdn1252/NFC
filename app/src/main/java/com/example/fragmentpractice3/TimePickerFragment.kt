package com.example.fragmentpractice3

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.text.DateFormat
import java.util.Calendar

class TimePickerFragment : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var c: Calendar = Calendar.getInstance()

        var hour = c.get(Calendar.HOUR_OF_DAY)
        var min = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, activity as OnTimeSetListener,
            // 실행안되면 이부분 확인
            hour, min, android.text.format.DateFormat.is24HourFormat(activity))
    }
}