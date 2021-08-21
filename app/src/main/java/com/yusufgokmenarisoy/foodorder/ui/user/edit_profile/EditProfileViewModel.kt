package com.yusufgokmenarisoy.foodorder.ui.user.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.SuccessResponse
import com.yusufgokmenarisoy.foodorder.data.entity.UpdateProfileBody
import com.yusufgokmenarisoy.foodorder.data.entity.User
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.util.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val token = args.get<String>("token")!!
    private val user: User = args.get<User>("user")!!
    private var image = when (user.image) {
        "1" -> R.drawable.avatar_1
        "2" -> R.drawable.avatar_2
        "3" -> R.drawable.avatar_3
        "4" -> R.drawable.avatar_4
        "5" -> R.drawable.avatar_5
        "6" -> R.drawable.avatar_6
        "7" -> R.drawable.avatar_7
        else -> R.drawable.avatar_8
    }

    fun getUser(): User = this.user

    fun setImage(image: Int) {
        this.image = image
    }

    fun getImage(): Int = this.image

    fun validateInputs(email: String, name: String, surname: String): Boolean {
        return email.length > 4 && Common.validateEmail(email) && name.length > 2 && surname.length > 2
    }

    private fun imageToString(): String = when (image) {
        R.drawable.avatar_1 -> "1"
        R.drawable.avatar_2 -> "2"
        R.drawable.avatar_3 -> "3"
        R.drawable.avatar_4 -> "4"
        R.drawable.avatar_5 -> "5"
        R.drawable.avatar_6 -> "6"
        R.drawable.avatar_7 -> "7"
        else -> "8"
    }

    fun updateProfile(email: String, name: String, surname: String, phoneNumber: String): LiveData<Resource<SuccessResponse>> =
        apiRepository.updateProfile(token, UpdateProfileBody(email, name, surname, imageToString(), phoneNumber))
}