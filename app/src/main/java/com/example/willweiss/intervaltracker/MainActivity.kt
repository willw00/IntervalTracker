package com.example.willweiss.intervaltracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.willweiss.intervaltracker.components.ProgressBarUpdatingCountDownTimer
import com.example.willweiss.intervaltracker.components.TimePickerChangeListener
import com.example.willweiss.intervaltracker.model.Interval
import com.example.willweiss.intervaltracker.model.IntervalSet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var timePickerListener: TimePickerChangeListener? = null

    private val intervalSet = IntervalSet(listOf(
            Interval("First!", 2),
            Interval("Second!", 5)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timePickerListener = TimePickerChangeListener(pickedTime)

        timePicker.setOnSeekBarChangeListener(timePickerListener)

        pickedTime.text = timePicker.progress.toString() + "s"
    }

    fun startTimer(view: View) {
//        val countdownTime = timePicker.progress * 1000
//        timerBar.max = countdownTime
        val intervalsIterator = intervalSet.intervals.iterator()

        intervalsIterator.withIndex().forEach { s ->
            timerBar.setProgress(0)

            print(s.value)
            currentIntervalName.text = s.value.name
            var nextName = if (intervalsIterator.hasNext()) intervalSet.intervals.get(s.index + 1).name else "Last Interval"

            nextIntervalName.text = nextName

            val countdownMillis = s.value.seconds * 1000
            timerBar.max = countdownMillis

            ProgressBarUpdatingCountDownTimer(timerBar, countdownMillis.toLong(), 100).start()
        }
    }
}
