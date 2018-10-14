package com.example.willweiss.intervaltracker.model

data class IntervalSet(val intervals: Collection<Interval>) {
    val totalTime = intervals.map { i -> i.seconds }
}