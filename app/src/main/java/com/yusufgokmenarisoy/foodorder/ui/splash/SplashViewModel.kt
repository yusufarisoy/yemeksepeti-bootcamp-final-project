package com.yusufgokmenarisoy.foodorder.ui.splash

import androidx.lifecycle.LiveData
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
    private val firstLaunch = apiRepository.getBoolean(FIRST_LAUNCH)

    fun getToken(): String? = this.token

    fun isFirstLaunch(): Boolean = this.firstLaunch

    fun removeToken() {
        apiRepository.saveString(TOKEN, "")
    }

    fun authorizeToken(token: String): LiveData<Resource<UserResponse>> = apiRepository.getUserProfile(token)

    fun saveFirstLaunch() {
        apiRepository.saveBoolean(FIRST_LAUNCH, false)
    }
}