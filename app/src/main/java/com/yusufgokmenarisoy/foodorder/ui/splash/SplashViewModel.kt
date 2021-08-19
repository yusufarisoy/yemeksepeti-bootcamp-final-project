package com.yusufgokmenarisoy.foodorder.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.UserResponse
import com.yusufgokmenarisoy.foodorder.data.local.FIRST_LAUNCH
import com.yusufgokmenarisoy.foodorder.data.local.TOKEN
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private val token = apiRepository.getString(TOKEN)
    private val firstLaunch: MutableLiveData<Boolean> = MutableLiveData()

    fun startApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            this.firstLaunch.value = apiRepository.getBoolean(FIRST_LAUNCH)
        }, 100)
    }

    fun getToken(): String? = token

    fun authorizeToken(token: String): LiveData<Resource<UserResponse>> = apiRepository.getUserProfile(token)

    fun isFirstLaunch(): LiveData<Boolean> = this.firstLaunch

    fun saveFirstLaunch() {
        apiRepository.saveBoolean(FIRST_LAUNCH, false)
    }
}