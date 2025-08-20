package org.example.app.repo

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.example.app.model.TrackItem

/**
 * PUBLIC_INTERFACE
 * LocalLibrary stores imported audio track references using shared preferences.
 */
class LocalLibrary private constructor(ctx: Context) {
    private val prefs: SharedPreferences = ctx.getSharedPreferences("library", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val type = object : TypeToken<MutableList<TrackItem>>() {}.type

    fun getAll(): List<TrackItem> {
        val json = prefs.getString(KEY, "[]") ?: "[]"
        return gson.fromJson(json, type)
    }

    fun addAll(items: List<TrackItem>) {
        val list = getAll().toMutableList()
        list.addAll(items)
        save(list)
    }

    fun remove(item: TrackItem) {
        val list = getAll().toMutableList()
        list.removeAll { it.uri == item.uri }
        save(list)
    }

    private fun save(list: MutableList<TrackItem>) {
        prefs.edit().putString(KEY, gson.toJson(list)).apply()
    }

    companion object {
        private const val KEY = "tracks"
        @Volatile private var INSTANCE: LocalLibrary? = null

        // PUBLIC_INTERFACE
        fun getInstance(ctx: Context): LocalLibrary {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocalLibrary(ctx.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
