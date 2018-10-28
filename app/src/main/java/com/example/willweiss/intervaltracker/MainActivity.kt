package com.example.willweiss.intervaltracker

import android.content.ClipData
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.view.Menu
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

    private val intervalSet = IntervalSet("Set1", listOf(
            Interval("First!", 5),
            Interval("Second!", 5)))

    private val intervalSet2 = IntervalSet("Set2", listOf(
            Interval("First2!", 5),
            Interval("Second2!", 5)))

    private val intervalSets = mapOf(0 to intervalSet, 1 to intervalSet2)

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

        navDrawer.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            drawer_layout.closeDrawers()
            true
        }

        intervalSets.forEach {it ->
            val newItem = navDrawer.menu.add(Menu.NONE, it.key, it.key, it.value.name)
            println(newItem)
        }

        timePickerListener = TimePickerChangeListener(pickedTime)

        timePicker.setOnSeekBarChangeListener(timePickerListener)

        pickedTime.text = timePicker.progress.toString() + "s"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menu?.clear()
//        menuInflater.inflate(R.menu.drawer_view, menu)
//        menu?.add(0, 0, Menu.NONE, "Option1")

        println(menu?.size())
        val intervals =
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
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
