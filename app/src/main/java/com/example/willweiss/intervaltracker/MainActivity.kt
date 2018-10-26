package com.example.willweiss.intervaltracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.view.MenuItem
import android.view.View
import com.example.willweiss.intervaltracker.components.ProgressBarUpdatingCountDownTimer
import com.example.willweiss.intervaltracker.components.TimePickerChangeListener
import com.example.willweiss.intervaltracker.model.Interval
import com.example.willweiss.intervaltracker.model.IntervalSet
import com.example.willweiss.intervaltracker.model.ProgressBarUpdate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var timePickerListener: TimePickerChangeListener? = null

    private val UPDATE_PROGRESS_BAR: Int = 1

    private val intervalSet = IntervalSet(listOf(
            Interval("First!", 10),
            Interval("Second!", 10)))

    private lateinit var mDrawerLayout: DrawerLayout

    val progressBarHandler = Handler() {message ->
        if (message.what == UPDATE_PROGRESS_BAR) {
            val progress = message.obj as ProgressBarUpdate

            timerBar.setProgress(0)

            println(progress.interval)
            currentIntervalName.text = progress.interval.name

            val countdownMillis = progress.interval.seconds * 1000
            timerBar.max = countdownMillis

            ProgressBarUpdatingCountDownTimer(timerBar, countdownMillis.toLong(), 100).start()
            true
        }
        else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        mDrawerLayout = drawer_layout

        timePickerListener = TimePickerChangeListener(pickedTime)

        timePicker.setOnSeekBarChangeListener(timePickerListener)

        pickedTime.text = timePicker.progress.toString() + "s"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun startTimer(view: View) {

        val background = Thread(Runnable {
            try {
                val intervalsIterator = intervalSet.intervals.iterator()
                intervalsIterator.withIndex().forEach { s ->
                    val countdownMillis = s.value.seconds * 1000
                    val message = progressBarHandler.obtainMessage(UPDATE_PROGRESS_BAR, ProgressBarUpdate(s.index, s.value))
                    progressBarHandler.sendMessage(message)
                    Thread.sleep(countdownMillis.toLong())
                }
            }
            catch (e: Exception) {
                println(e.message)
            }
        })

        background.start()
    }
}
