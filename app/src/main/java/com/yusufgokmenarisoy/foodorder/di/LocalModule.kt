package com.yusufgokmenarisoy.foodorder.di

import android.content.Context
import androidx.room.Room
import com.yusufgokmenarisoy.foodorder.data.local.CartDao
import com.yusufgokmenarisoy.foodorder.data.local.LocalDataSource
import com.yusufgokmenarisoy.foodorder.data.local.LocalDatabase
import com.yusufgokmenarisoy.foodorder.data.local.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
class LocalModule {

    @Provides
    fun provideCartDao(localDatabase: LocalDatabase): CartDao = localDatabase.cartDao()

    @Provides
    fun provideLocalDataSource(sharedPrefManager: SharedPrefManager, cartDao: CartDao): LocalDataSource =
        LocalDataSource(sharedPrefManager, cartDao)

    @Provides
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager = SharedPrefManager(context)

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room
            .databaseBuilder(context, LocalDatabase::class.java, "LocalDatabase")
            .build()
    }
}