package com.yusufgokmenarisoy.foodorder.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.AddressResponse
import com.yusufgokmenarisoy.foodorder.data.entity.User
import com.yusufgokmenarisoy.foodorder.data.local.TOKEN
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private var token = apiRepository.getString(TOKEN)
    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User>
        get() = _user

    lateinit var addresses: LiveData<Resource<AddressResponse>>
    private var addressInitialized = false
    private val _cartItemCount: MutableLiveData<Int> = MutableLiveData()
    val cartItemCount: LiveData<Int>
        get() = _cartItemCount

    private var updateOrders: Boolean = false

    private var updateRestaurants: Boolean = false

    init {
        getCartItemCount()
    }

    fun getToken(): String? = this.token

    fun setToken(token: String) {
        this.token = token
    }

    fun setUser(user: User) {
        this._user.value = user
    }

    fun updateUser(image: String, email: String, name: String, surname: String, phoneNumber: String) {
        val user = this._user.value!!
        user.image = image
        user.email = email
        user.name = name
        user.surname = surname
        user.phoneNumber = phoneNumber
        this._user.value = user
    }

    fun getAddresses() {
        if (!addressInitialized) {
            addressInitialized = true
            addresses = apiRepository.getAddressesOfUser(token!!)
        }
    }

    fun updateAddresses() {
        addresses = apiRepository.getAddressesOfUser(token!!)
    }

    fun getCartItemCount() {
        viewModelScope.launch {
            _cartItemCount.value = apiRepository.getItemCount()
        }
    }

    fun setItemCount(count: Int) {
        _cartItemCount.value = count
    }

    fun increaseCartItemCount() {
        val count = _cartItemCount.value
        _cartItemCount.value = count!! + 1
    }

    fun decreaseCartItemCount() {
        val count = _cartItemCount.value
        _cartItemCount.value = count!! - 1
    }

    fun isUpdateOrders(): Boolean = this.updateOrders

    fun newOrder() {
        updateOrders = true
    }

    fun ordersUpdated() {
        updateOrders = false
    }

    fun isUpdateRestaurants(): Boolean = this.updateRestaurants

    fun restaurantsChanged() {
        updateRestaurants = true
    }

    fun restaurantsUpdated() {
        updateRestaurants = false
    }

    fun getUserImage(): Int = when (_user.value!!.image) {
        "2" -> R.drawable.avatar_2
        "3" -> R.drawable.avatar_3
        "4" -> R.drawable.avatar_4
        "5" -> R.drawable.avatar_5
        "6" -> R.drawable.avatar_6
        "7" -> R.drawable.avatar_7
        "8" -> R.drawable.avatar_8
        else -> R.drawable.avatar_1
    }
}