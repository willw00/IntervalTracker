package com.example.willweiss.intervaltracker.components

import android.widget.SeekBar
import android.widget.TextView


class TimePickerChangeListener(val textView: TextView): SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        textView.text = progress.toString() + "s"
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}