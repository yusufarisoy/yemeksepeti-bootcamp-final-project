package com.yusufgokmenarisoy.foodorder.data.local

import androidx.lifecycle.LiveData
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val sharedPrefManager: SharedPrefManager,
    private val cartDao: CartDao
    ) {

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

    //Database
    suspend fun add(cartItem: CartItem) {
        this.cartDao.add(cartItem)
    }

    fun getCart(): LiveData<List<CartItem>> = this.cartDao.getCart()

    suspend fun getItemCount(): Int = this.cartDao.getItemCount()

    fun getById(id: Int): LiveData<CartItem?> = this.cartDao.getById(id)

    suspend fun update(cartItem: CartItem) {
        this.cartDao.update(cartItem)
    }

    suspend fun delete(cartItem: CartItem) {
        this.cartDao.delete(cartItem)
    }

    suspend fun clear() {
        this.cartDao.clear()
    }
}