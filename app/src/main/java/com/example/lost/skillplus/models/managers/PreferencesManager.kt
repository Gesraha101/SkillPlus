package com.example.lost.skillplus.models.managers

import android.content.Context
import android.content.SharedPreferences
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.podos.raw.Schedule
import com.example.lost.skillplus.models.podos.raw.User
import com.google.gson.Gson


class PreferencesManager(val context: Context) {

    private inline fun Context.putData(func: SharedPreferences.Editor.() -> Unit) {
        val sharedPreferences = getSharedPreferences(Keys.MY_PREFERENCES.key, Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        editor.func()
        editor.apply()
    }

    private inline fun Context.getData(func: SharedPreferences.() -> Any): Any {
        val sharedPreferences = getSharedPreferences(Keys.MY_PREFERENCES.key, Context.MODE_PRIVATE)!!
        return sharedPreferences.func()
    }

    fun getLastUpdated(): Long {
        return context.getData { getLong(Keys.LAST_UPDATED.key, 0) } as Long
    }

    fun setLastUpdated(lastUpdated: Long) {
        context.putData { putLong(Keys.LAST_UPDATED.key, lastUpdated) }
    }

    fun getSchedules(): Set<String>? {
        return context.getData { getStringSet(Keys.SCHEDULES.key, emptySet())!! } as Set<String>
    }

    fun addToSchedules(schedule: Schedule) {
        context.putData { putStringSet(Keys.SCHEDULES.key, getSchedules()?.plusElement(Gson().toJson(schedule))) }
    }

    fun removeFromSchedules(schedule: Schedule) {
        context.putData { putStringSet(Keys.SCHEDULES.key, getSchedules()?.plusElement(Gson().toJson(schedule))) }
    }

    fun getUser(): User {
        val json = context.getData { getString(Keys.ACTIVE_USER.key, null)!! } as String
        return Gson().fromJson<User>(json, User::class.java)
    }

    fun setUser(user: User) {
        context.putData { putString(Keys.ACTIVE_USER.key, Gson().toJson(user)) }
    }

    fun getFlag(): Boolean {
        return context.getData { getBoolean("flag", false) } as Boolean
    }

    fun setFlag(flag: Boolean) {
        context.putData { putBoolean("flag", flag) }
    }

    fun getId(): Int {
        return context.getData { getInt("id", 0) } as Int
    }

    fun setId(id: Int) {
        context.putData { putInt("id", id) }
    }

    fun getName(): String? {
        return context.getData { getString("name", "") } as String
    }

    fun setName(name: String) {
        context.putData { putString("name", name) }
    }
}