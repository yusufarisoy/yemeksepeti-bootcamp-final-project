package com.yusufgokmenarisoy.foodorder.ui.user.order_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.*
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val order: UserOrder = args.get<UserOrder>("order")!!
    lateinit var foodList: LiveData<Resource<FoodListOfOrderResponse>>

    fun getOrder(): UserOrder = this.order

    fun getFoodListOfOrder(token: String) {
        this.foodList = apiRepository.getFoodsOfOrder(token, order.id)
    }

    fun getRating(): LiveData<Resource<RatingListResponse>> = apiRepository.getRatings(null, this.order.id)

    fun createRating(token: String, score: Int, review: String): LiveData<Resource<SuccessResponse>> =
        apiRepository.createRating(token, RatingBody(order.restaurantId, order.id, score, review))
}