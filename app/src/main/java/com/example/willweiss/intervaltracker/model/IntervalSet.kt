package com.example.willweiss.intervaltracker.model

data class IntervalSet(val intervals: List<Interval>) {
    val totalTime = intervals.map { i -> i.seconds }.sum()
}