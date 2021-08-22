package com.yusufgokmenarisoy.foodorder.ui.owner.order_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.FoodListOfOrderResponse
import com.yusufgokmenarisoy.foodorder.data.entity.OrderStatusUpdateBody
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantOrder
import com.yusufgokmenarisoy.foodorder.data.entity.SuccessResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnerOrderDetailViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val restaurantId: Int = args.get<Int>("restaurantId")!!
    private val order: RestaurantOrder = args.get<RestaurantOrder>("order")!!
    lateinit var foodList: LiveData<Resource<FoodListOfOrderResponse>>

    fun getOrder(): RestaurantOrder = this.order


    fun getFoodListOfOrder(token: String) {
        this.foodList = apiRepository.getFoodsOfOrder(token, order.id)
    }

    fun updateOrderStatus(token: String, newStatus: Int): LiveData<Resource<SuccessResponse>> =
        apiRepository.updateOrderStatus(token, this.restaurantId, this.order.id, OrderStatusUpdateBody(newStatus))
}