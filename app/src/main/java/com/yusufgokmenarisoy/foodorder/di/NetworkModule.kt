package com.yusufgokmenarisoy.foodorder.di

import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.yusufgokmenarisoy.foodorder.data.remote.ApiService
import com.yusufgokmenarisoy.foodorder.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityRetainedComponent::class)
class NetworkModule {

    @Provides
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource = RemoteDataSource(apiService)

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        endPoint: String
    ): Retrofit = Retrofit.Builder().baseUrl(endPoint)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient).build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.interceptors().add(HttpLoggingInterceptor().apply {
            level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        return builder.build()
    }

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideEndPoint(): String = "https://yemeksepeti-bootcamp-project.herokuapp.com/api/"
}