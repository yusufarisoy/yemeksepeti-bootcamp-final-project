package com.yusufgokmenarisoy.foodorder.ui.user.order_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.UserOrderListResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    lateinit var orders: LiveData<Resource<UserOrderListResponse>>
    private var ordersInitialized = false

    fun getOrderHistory(token: String) {
        if (!ordersInitialized) {
            ordersInitialized = true
            orders = apiRepository.getOrderHistoryOfUser(token)
        }
    }
}