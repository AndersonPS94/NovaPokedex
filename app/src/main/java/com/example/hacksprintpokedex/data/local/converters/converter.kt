// app/src/main/java/com/example/hacksprintpokedex/data/local/converters/ListStringConverter.kt
package com.example.hacksprintpokedex.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ListStringConverter {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }
}