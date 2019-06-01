package com.example.willweiss.intervaltracker.components

import android.content.Context
import com.beust.klaxon.*
import com.example.willweiss.intervaltracker.model.Interval
import com.example.willweiss.intervaltracker.model.IntervalSet
import org.json.JSONObject
import java.io.File


interface DAO {
    fun saveIntervalSet(context: Context, set: IntervalSet)
    fun loadIntervalSets(context: Context): List<IntervalSet?>
}

class LocalFileDAO: DAO {

    private val intervalDirectory = "intervals"

    private val interalSetConverter = object: Converter {
        override fun toJson(value: Any): String {
            val iSet = (value as IntervalSet)
            return iSet.toJson()
        }

        override fun fromJson(jv: JsonValue): Any? {
            val jsonObject = jv.obj
            val name = jsonObject?.string("name")
            val intervals = jsonObject?.array<JsonObject>("intervals")?.map {j ->
                Interval(j.string("name")!!, j.int("seconds")!!)
            }

            return IntervalSet(name!!, intervals!!)
        }

        override fun canConvert(cls: Class<*>): Boolean = cls == IntervalSet::class.java

    }

    override fun saveIntervalSet(context: Context, set: IntervalSet) {
        val jsonString = set.toJson()
        val fileName = set.name.toLowerCase().replace(" ", "_").plus(".json")

        val newFile = File(context.filesDir, fileName)
        newFile.writeText(jsonString)
    }

    override fun loadIntervalSets(context: Context): List<IntervalSet?> {

        val dir = context.filesDir
        val sets = dir.listFiles().map {f ->
            f.useLines { lines ->
                    Klaxon()
                    .converter(interalSetConverter)
                    .parse<IntervalSet>(lines.first())
            }
        }

        return sets.sortedBy { it?.name }
    }
}