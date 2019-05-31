package com.example.willweiss.intervaltracker.components

import android.content.Context
import com.beust.klaxon.*
import com.example.willweiss.intervaltracker.model.Interval
import com.example.willweiss.intervaltracker.model.IntervalSet
import java.io.File


interface DAO {
    fun saveIntervalSets(context: Context, sets: List<IntervalSet>)
    fun loadIntervalSets(context: Context): List<IntervalSet?>
}

class LocalFileDAO: DAO {

    private val fileName: String = "interval_sets.json"

    private val interalSetConverter = object: Converter {
        override fun toJson(value: Any): String {
            val iSet = (value as IntervalSet)
            return iSet.toJson()
        }

        override fun fromJson(jv: JsonValue): Any? {
            val jsonObject = jv.obj
            val name = jsonObject?.string("name")
            val intervals = jsonObject?.array<Interval>("intervals")
            return IntervalSet(name!!, intervals!!)
        }

        override fun canConvert(cls: Class<*>): Boolean = cls == IntervalSet::class.java

    }

    override fun saveIntervalSets(context: Context, sets: List<IntervalSet>) {
        val jsonStrings = sets.map { s -> s.toJson() }.joinToString(separator = "\n")

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {f ->
            f.write(jsonStrings.toByteArray())
        }
    }

    override fun loadIntervalSets(context: Context): List<IntervalSet?> {

        return File(fileName).useLines { lines ->
            lines.map { line ->
                Klaxon()
                .converter(interalSetConverter)
                .parse<IntervalSet>(line)
            }.toList()
        }
    }
}