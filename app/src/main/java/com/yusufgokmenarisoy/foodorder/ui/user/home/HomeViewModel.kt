package com.yusufgokmenarisoy.foodorder.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.*
import com.yusufgokmenarisoy.foodorder.data.local.TOKEN
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private lateinit var user: User
    private lateinit var token: String

    fun getUser(): User = this.user

    fun setUser(user: User) {
        this.user = user
    }

    fun getToken(): String = this.token

    fun setToken(token: String) {
        this.token = token
    }

    fun getPopularRestaurants(cityId: Int): LiveData<Resource<RestaurantsResponse>> = apiRepository.getMostPopularRestaurants(cityId)

    fun onRestaurantClicked(restaurant: Restaurant) {
        //TODO: navigate to restaurant detail
    }

    fun getAddresses(token: String): LiveData<Resource<AddressResponse>> = apiRepository.getAddressesOfUser(token)

    fun onAddressClicked(address: Address) {
        //TODO: navigate to restaurants page with address
    }

    fun getOrderHistory(token: String): LiveData<Resource<UserOrderResponse>> = apiRepository.getOrderHistoryOfUser(token)

    fun onOrderClicked(order: UserOrder) {
        //TODO: navigate to rating
    }
}