package com.yusufgokmenarisoy.foodorder.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.LoginBody
import com.yusufgokmenarisoy.foodorder.data.entity.LoginResponse
import com.yusufgokmenarisoy.foodorder.data.local.TOKEN
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.util.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    var isPasswordValid = false

    fun login(email: String, password: String): LiveData<Resource<LoginResponse>> = apiRepository.login(LoginBody(email, password))

    fun validateInputs(email: String): Boolean = Common.validateEmail(email) && isPasswordValid

    fun saveToken(token: String) {
        apiRepository.saveString(TOKEN, token)
    }
}