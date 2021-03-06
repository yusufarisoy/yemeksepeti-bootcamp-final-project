package com.yusufgokmenarisoy.foodorder.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.ChangePasswordBody
import com.yusufgokmenarisoy.foodorder.data.entity.SuccessResponse
import com.yusufgokmenarisoy.foodorder.data.local.TOKEN
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.util.Common.Companion.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    fun controlPasswords(currentPassword: String, newPassword: String, newPasswordRepeat: String): Boolean {
        return newPassword == newPasswordRepeat && currentPassword.length > 3 && newPassword.length > 3 &&
                currentPassword.validatePassword() && newPassword.validatePassword()
    }

    fun changePassword(token: String, currentPassword: String, newPassword: String): LiveData<Resource<SuccessResponse>> =
        apiRepository.changePassword(token, ChangePasswordBody(currentPassword, newPassword))

    fun logout() {
        apiRepository.saveString(TOKEN, "")
    }
}