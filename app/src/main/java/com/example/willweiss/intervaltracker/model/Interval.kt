package com.example.willweiss.intervaltracker.model

data class Interval(val name: String, val seconds: Int, val restSeconds: Int? = null) {
    private val restJson = if (restSeconds != null) {
        ""","rest_seconds:""""
    } else """"""

    fun toJson(): String = """{"name":"$name","seconds":$seconds$restJson}"""
}