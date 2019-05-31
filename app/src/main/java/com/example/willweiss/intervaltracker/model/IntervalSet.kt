package com.example.willweiss.intervaltracker.model

data class IntervalSet(val name: String, val intervals: List<Interval>) {
    val totalTime = intervals.map { i -> i.seconds }.sum()

    fun toJson(): String = """{"name":$name, "intervals":[${intervals.map{ it.toJson() }.joinToString()}]}"""
}