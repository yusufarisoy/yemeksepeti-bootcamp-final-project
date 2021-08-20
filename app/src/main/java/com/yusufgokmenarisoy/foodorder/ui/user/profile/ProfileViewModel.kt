package com.yusufgokmenarisoy.foodorder.ui.user.profile

import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.local.TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    fun logout() {
        apiRepository.saveString(TOKEN, "")
    }
}