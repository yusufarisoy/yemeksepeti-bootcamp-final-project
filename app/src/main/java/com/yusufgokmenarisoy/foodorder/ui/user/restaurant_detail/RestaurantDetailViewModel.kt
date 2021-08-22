package com.yusufgokmenarisoy.foodorder.ui.user.restaurant_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.FoodListResponse
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val restaurant = args.get<Restaurant>("restaurant")!!
    lateinit var menu: LiveData<Resource<FoodListResponse>>

    init {
        getRestaurantMenu()
    }

    fun getRestaurant(): Restaurant = this.restaurant

    private fun getRestaurantMenu() {
        menu = apiRepository.getRestaurantMenu(restaurant.id)
    }
}