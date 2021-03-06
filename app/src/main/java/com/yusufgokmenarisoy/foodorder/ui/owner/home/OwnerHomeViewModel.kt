package com.yusufgokmenarisoy.foodorder.ui.owner.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantListResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnerHomeViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    lateinit var restaurants: LiveData<Resource<RestaurantListResponse>>
    private var isRestaurantsInitialized = false

    fun getRestaurantsByOwnerId(ownerId: Int) {
        if (!isRestaurantsInitialized) {
            isRestaurantsInitialized = true
            restaurants = apiRepository.getRestaurants(ownerId, null, null)
        }
    }

    fun updateRestaurants(ownerId: Int) {
        restaurants = apiRepository.getRestaurants(ownerId, null, null)
    }
}