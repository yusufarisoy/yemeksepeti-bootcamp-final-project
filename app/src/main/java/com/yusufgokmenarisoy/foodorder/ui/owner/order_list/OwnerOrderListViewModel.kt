package com.yusufgokmenarisoy.foodorder.ui.owner.order_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantOrderListResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnerOrderListViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    val restaurantId: Int = args.get<Int>("restaurantId")!!
    lateinit var orders: LiveData<Resource<RestaurantOrderListResponse>>

    fun getReceivedOrders(token: String) {
        this.orders = apiRepository.getOrdersOfRestaurant(token, this.restaurantId, 1)
    }

    fun getActiveOrders(token: String) {
        this.orders = apiRepository.getOrdersOfRestaurant(token, this.restaurantId, 2)
    }

    fun getOnTheWayOrders(token: String) {
        this.orders = apiRepository.getOrdersOfRestaurant(token, this.restaurantId, 3)
    }
}