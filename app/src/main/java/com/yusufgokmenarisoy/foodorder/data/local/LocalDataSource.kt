package com.yusufgokmenarisoy.foodorder.data.local

import javax.inject.Inject

class LocalDataSource @Inject constructor(private val sharedPrefManager: SharedPrefManager) {

    fun getString(key: String): String? = sharedPrefManager.getString(key)

    fun saveString(key: String, data: String) {
        sharedPrefManager.saveString(key, data)
    }

    fun saveInt(key: String, data: Int) {
        sharedPrefManager.saveInt(key, data)
    }

    fun getInt(key: String): Int = sharedPrefManager.getInt(key)

    fun saveBoolean(key: String, data: Boolean) {
        sharedPrefManager.saveBoolean(key, data)
    }

    fun getBoolean(key: String): Boolean = sharedPrefManager.getBoolean(key)
}