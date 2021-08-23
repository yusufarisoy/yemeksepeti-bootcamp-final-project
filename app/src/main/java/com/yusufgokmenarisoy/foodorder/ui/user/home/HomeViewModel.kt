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

    private val token = apiRepository.getString(TOKEN)
    lateinit var restaurants: LiveData<Resource<RestaurantListResponse>>
    lateinit var orders: LiveData<Resource<UserOrderListResponse>>
    private var ordersInitialized = false
    private var restaurantsInitialized = false

    init {
        getOrderHistory(token!!)
    }

    fun getPopularRestaurants(cityId: Int) {
        if (!restaurantsInitialized) {
            restaurantsInitialized = true
            restaurants = apiRepository.getMostPopularRestaurants(cityId)
        }
    }

    fun getOrderHistory(token: String) {
        if (!ordersInitialized) {
            ordersInitialized = true
            orders = apiRepository.getOrderHistoryOfUser(token)
        }
    }

    fun updateOrderHistory(token: String) {
        orders = apiRepository.getOrderHistoryOfUser(token)
    }
}