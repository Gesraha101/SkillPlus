package com.example.lost.skillplus.models.managers

import android.content.Context
import com.example.lost.skillplus.models.enums.Keys

class PreferencesManager(context: Context) {
    private val myPreferences = "myPrefs"
    private val sharedPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)!!

    fun getId(): Int {
        return sharedPreferences.getInt("id", 0)
    }

    fun setId(id: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("id", id)
        editor.apply()
    }


    fun getName(): String? {
        return sharedPreferences.getString("name", "")
    }

    fun setName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.apply()
    }

    fun getSchedule(): MutableSet<String>? {
        return sharedPreferences.getStringSet(Keys.TIMESTAMPS.key, null)
    }
}