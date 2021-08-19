package com.yusufgokmenarisoy.foodorder.di

import android.content.Context
import com.yusufgokmenarisoy.foodorder.data.local.LocalDataSource
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
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager = SharedPrefManager(context)

    @Provides
    fun provideLocalDataSource(sharedPrefManager: SharedPrefManager): LocalDataSource = LocalDataSource(sharedPrefManager)
}