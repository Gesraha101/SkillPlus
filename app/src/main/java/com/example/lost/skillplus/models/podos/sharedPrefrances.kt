package com.example.lost.skillplus.models.podos

import android.content.Context

class shared(context: Context) {
    val myPreferences = "myPrefs"
    val sharedPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

    fun getId(): String {
        return sharedPreferences.getString("id", "0")
    }

    fun setId(id: String) {
        val editor = sharedPreferences.edit()
        editor.putString("id", id)
        editor.apply()
    }


    fun getName(): String {
        return sharedPreferences.getString("name", "")
    }

    fun setName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.apply()
    }
}