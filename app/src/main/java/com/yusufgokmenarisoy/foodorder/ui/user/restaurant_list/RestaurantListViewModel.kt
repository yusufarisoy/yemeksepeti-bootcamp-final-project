package com.yusufgokmenarisoy.foodorder.ui.user.restaurant_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.Address
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantListResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val address = args.get<Address>("address")!!
    lateinit var restaurants: LiveData<Resource<RestaurantListResponse>>

    init {
        getRestaurantsByDistrict()
    }

    fun getAddress(): Address = this.address

    private fun getRestaurantsByDistrict() {
        this.restaurants = apiRepository.getRestaurants(null, null, address.districtId)
    }

    private fun getRestaurantsByCity() {
        this.restaurants = apiRepository.getRestaurants(null, address.cityId, null)
    }
}