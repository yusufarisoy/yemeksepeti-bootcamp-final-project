package com.yusufgokmenarisoy.foodorder.ui.owner.restaurant_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnerRestaurantDetailViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val restaurant: Restaurant = args.get<Restaurant>("restaurant")!!

    fun getRestaurant(): Restaurant = this.restaurant
}