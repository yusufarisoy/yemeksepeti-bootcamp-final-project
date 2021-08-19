package com.yusufgokmenarisoy.foodorder.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.RegisterBody
import com.yusufgokmenarisoy.foodorder.data.entity.SuccessResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.util.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    var isPasswordValid = false

    fun register(role: Int, email: String, password: String, name: String, surname: String):
            LiveData<Resource<SuccessResponse>> = apiRepository.register(RegisterBody(role, email, password, name, surname, "", ""))

    fun validateInputs(email: String, name: String, surname: String): Boolean =
        Common.validateEmail(email) && isPasswordValid && name.length > 2 && surname.length > 1
}