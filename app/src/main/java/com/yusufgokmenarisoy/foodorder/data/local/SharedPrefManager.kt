package com.yusufgokmenarisoy.foodorder.data.local

import android.content.Context
import android.content.SharedPreferences

const val TOKEN = "com.yusufgokmenarisoy.foodorder.TOKEN"
const val FIRST_LAUNCH = "com.yusufgokmenarisoy.foodorder.FIRST_LAUNCH"

class SharedPrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

    fun getString(key: String): String? = sharedPreferences.getString(key, "")

    fun saveString(key: String, data: String) {
        sharedPreferences.edit().putString(key, data).apply()
    }

    fun getInt(key: String): Int = sharedPreferences.getInt(key, -1)

    fun saveInt(key: String, data: Int) {
        sharedPreferences.edit().putInt(key, data).apply()
    }

    fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, true)

    fun saveBoolean(key: String, data: Boolean) {
        sharedPreferences.edit().putBoolean(key, data).apply()
    }
}