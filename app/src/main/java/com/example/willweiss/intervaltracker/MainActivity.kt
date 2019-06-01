package com.example.willweiss.intervaltracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.willweiss.intervaltracker.components.LocalFileDAO
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

    private var activeIntervalSet = intervalSet

    private val progressBarHandler = Handler() { message ->
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

    private fun setNav(navDrawer: NavigationView) = run {
        intervalSets.forEach {it ->
            val newItem = navDrawer.menu.add(Menu.NONE, it.key, it.key, it.value.name)
            println(newItem)
        }

        navDrawer.setNavigationItemSelectedListener { menuItem ->
            val itemId = menuItem.itemId
            val selectedInterval = intervalSets.getValue(itemId)

            setActiveInterval(selectedInterval)
            drawer_layout.closeDrawers()
            true
        }
    }

    private fun setTimePicker(pickedTime: TextView) = run {
        timePickerListener = TimePickerChangeListener(pickedTime)

        timePicker.setOnSeekBarChangeListener(timePickerListener)

        pickedTime.text = timePicker.progress.toString() + "s"
    }

    private fun setActiveInterval(intervalSet: IntervalSet) = run {
        activeIntervalSet = intervalSet
        supportActionBar?.apply {
            setTitle(intervalSet.name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(activeIntervalSet.name)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        setNav(navDrawer)

        setTimePicker(pickedTime)

        val localDAO = LocalFileDAO()
        localDAO.saveIntervalSet(this, intervalSet)
        localDAO.saveIntervalSet(this, intervalSet2)

        localDAO.loadIntervalSets(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.interval_view_menu, menu)
        return true
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
                val intervalsIterator = activeIntervalSet.intervals.iterator()
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
