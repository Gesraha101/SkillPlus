package com.example.lost.skillplus.models.managers

import android.content.Context
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.podos.raw.User
import com.google.gson.Gson



class PreferencesManager(context: Context) {
    private val myPreferences = "myPrefs"
    private val sharedPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)!!

    fun getLastUpdated(): Long {
        return sharedPreferences.getLong(Keys.LAST_UPDATED.key, 0)
    }

    fun setLastUpdated(lastUpdated: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(Keys.LAST_UPDATED.key, lastUpdated)
        editor.apply()
    }

    fun getUser(): User {
        val json = sharedPreferences.getString(Keys.ACTIVE_USER.key, null)!!
        return Gson().fromJson<User>(json, User::class.java)
    }

    fun setUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(Keys.ACTIVE_USER.key, Gson().toJson(user))
        editor.apply()
    }

    fun getFlag(): Boolean {
        return sharedPreferences.getBoolean("flag", false)
    }

    fun setFlag(flag: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("flag", flag)
        editor.apply()
    }

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