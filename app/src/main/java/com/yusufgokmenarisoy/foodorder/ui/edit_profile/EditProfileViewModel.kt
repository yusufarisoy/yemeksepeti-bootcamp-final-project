package com.yusufgokmenarisoy.foodorder.ui.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.SuccessResponse
import com.yusufgokmenarisoy.foodorder.data.entity.UpdateProfileBody
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.util.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    fun validateInputs(email: String, name: String, surname: String): Boolean {
        return email.length > 4 && Common.validateEmail(email) && name.length > 2 && surname.length > 2
    }

    private fun imageToString(image: Int): String = when (image) {
        R.drawable.avatar_1 -> "1"
        R.drawable.avatar_2 -> "2"
        R.drawable.avatar_3 -> "3"
        R.drawable.avatar_4 -> "4"
        R.drawable.avatar_5 -> "5"
        R.drawable.avatar_6 -> "6"
        R.drawable.avatar_7 -> "7"
        else -> "8"
    }

    fun updateProfile(token: String, email: String, name: String, surname: String, image: Int, phoneNumber: String): LiveData<Resource<SuccessResponse>> =
        apiRepository.updateProfile(token, UpdateProfileBody(email, name, surname, imageToString(image), phoneNumber))
}