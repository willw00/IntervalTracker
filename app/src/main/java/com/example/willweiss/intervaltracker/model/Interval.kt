package com.example.willweiss.intervaltracker.model

data class Interval(val name: String, val seconds: Int) {
    fun toJson(): String = """{"name":"$name","seconds":$seconds}"""
}