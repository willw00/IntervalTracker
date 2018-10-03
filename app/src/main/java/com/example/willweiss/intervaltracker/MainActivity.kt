package com.example.willweiss.intervaltracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.willweiss.intervaltracker.components.TimePickerChangeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var timePickerListener: TimePickerChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timePickerListener = TimePickerChangeListener(pickedTime)
        timePicker.setOnSeekBarChangeListener(timePickerListener)
        pickedTime.text = timePicker.progress.toString() + "s"
    }

    fun incrementProgressBar(view: View) {
        val progress = timerBar.progress
        val nextProgress =
                if (progress + 10 > timerBar.max) timerBar.max
                else progress + 10
        timerBar.setProgress(nextProgress, true)
    }
}
