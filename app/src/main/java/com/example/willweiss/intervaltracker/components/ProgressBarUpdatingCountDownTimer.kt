package com.example.willweiss.intervaltracker.components

import android.os.CountDownTimer
import android.widget.ProgressBar

class ProgressBarUpdatingCountDownTimer(val progressBar: ProgressBar, val millisInFuture: Long, countdownInterval: Long): CountDownTimer(millisInFuture, countdownInterval) {
    override fun onTick(millisUntilFinished: Long) {
        val nextProgress =
                if (millisInFuture - millisUntilFinished > progressBar.max) progressBar.max
                else (millisInFuture - millisUntilFinished).toInt()

        progressBar.setProgress(nextProgress, true)
    }

    override fun onFinish() {
        println("ProgressBarMax: " + progressBar.max + ", Progress: " + progressBar.progress)
        progressBar.setProgress(progressBar.max)
    }
}