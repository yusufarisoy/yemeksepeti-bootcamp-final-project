package com.yusufgokmenarisoy.foodorder.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.*
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val user: User = args.get<User>("user")!!
    private val token: String = args.get<String>("token")!!

    lateinit var restaurants: LiveData<Resource<RestaurantResponse>>
    lateinit var addresses: LiveData<Resource<AddressResponse>>
    lateinit var orders: LiveData<Resource<UserOrderResponse>>

    init {
        getPopularRestaurants(user.cityId?: 1)
        getAddresses(token)
        getOrderHistory(token)
    }

    fun getUser(): User = this.user

    private fun getPopularRestaurants(cityId: Int) {
        restaurants = apiRepository.getMostPopularRestaurants(cityId)
    }

    private fun getAddresses(token: String) {
        addresses = apiRepository.getAddressesOfUser(token)
    }

    private fun getOrderHistory(token: String) {
        orders = apiRepository.getOrderHistoryOfUser(token)
    }
}