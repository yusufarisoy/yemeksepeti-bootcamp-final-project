package com.yusufgokmenarisoy.foodorder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem

@Database(entities = [CartItem::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
}