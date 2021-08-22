package com.yusufgokmenarisoy.foodorder.ui.owner.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.*
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class OwnerRestaurantViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val restaurant: Restaurant? = args.get<Restaurant>("restaurant")
    lateinit var cities: LiveData<Resource<CityListResponse>>

    init {
        getCities()
    }

    fun getRestaurant(): Restaurant? = this.restaurant

    private fun getCities() {
        this.cities = apiRepository.getCities()
    }

    fun getDistricts(cityId: Int): LiveData<Resource<DistrictListResponse>> = apiRepository.getDistricts(cityId)

    fun validateInputs(bannerImage: String, image: String, name: String, phoneNumber: String, minOrderFee: String, avgDeliveryTime: String): Boolean {
        return try {
            bannerImage.isNotEmpty() && image.isNotEmpty() && name.isNotEmpty() && phoneNumber.isNotEmpty() && minOrderFee.isNotEmpty() &&
                    minOrderFee.toInt() > 0 && avgDeliveryTime.isNotEmpty()
        } catch (ex: NumberFormatException) {
            false
        }
    }

    fun createRestaurant(token: String, restaurantBody: RestaurantBody): LiveData<Resource<SuccessResponse>> = apiRepository.createRestaurant(token, restaurantBody)

    fun updateRestaurant(token: String, restaurantId: Int, restaurantBody: RestaurantBody): LiveData<Resource<SuccessResponse>> =
        apiRepository.updateRestaurant(token, restaurantId, restaurantBody)

    fun deleteRestaurant(token: String, restaurantId: Int): LiveData<Resource<SuccessResponse>> = apiRepository.deleteRestaurant(token, restaurantId)
}