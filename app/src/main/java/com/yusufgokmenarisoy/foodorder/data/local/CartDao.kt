package com.yusufgokmenarisoy.foodorder.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem

@Dao
interface CartDao {

    @Insert
    suspend fun add(cartItem: CartItem)

    @Query("SELECT * FROM cart")
    fun getCart(): LiveData<List<CartItem>>

    @Query("SELECT COUNT(food_id) FROM cart")
    suspend fun getItemCount(): Int

    @Query("SELECT * FROM cart WHERE food_id = :id")
    fun getById(id: Int): LiveData<CartItem?>

    @Update
    suspend fun update(cartItem: CartItem)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart")
    suspend fun clear()
}