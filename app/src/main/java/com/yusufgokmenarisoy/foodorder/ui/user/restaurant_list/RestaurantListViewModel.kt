package com.yusufgokmenarisoy.foodorder.ui.user.restaurant_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.Address
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantsResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private lateinit var address: Address

    fun getAddress(): Address = this.address

    fun setAddress(address: Address) {
        this.address = address
    }

    fun getRestaurants(): LiveData<Resource<RestaurantsResponse>> = apiRepository.getRestaurantsByDistrict(address.districtId)
}