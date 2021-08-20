package com.yusufgokmenarisoy.foodorder.data.remote

import com.yusufgokmenarisoy.foodorder.data.entity.LoginBody
import com.yusufgokmenarisoy.foodorder.data.entity.RegisterBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource() {

    suspend fun login(loginBody: LoginBody) = getResult { apiService.login(loginBody) }

    suspend fun register(registerBody: RegisterBody) = getResult { apiService.register(registerBody) }

    suspend fun getUserProfile(token: String) = getResult { apiService.getUserProfile("Bearer $token") }

    suspend fun getAddressesOfUser(token: String) = getResult { apiService.getAddressesOfUser("Bearer $token") }

    suspend fun getOrderHistoryOfUser(token: String) = getResult { apiService.getOrderHistoryOfUser("Bearer $token") }

    //Restaurants

    suspend fun getRestaurantsByDistrict(districtId: Int) = getResult { apiService.getRestaurantsByDistrict(districtId) }

    suspend fun getRestaurantsByCity(cityId: Int) = getResult { apiService.getRestaurantsByCity(cityId) }

    suspend fun getMostPopularRestaurants(cityId: Int) = getResult { apiService.getMostPopularRestaurants(cityId) }

    suspend fun getRestaurantMenu(restaurantId: Int) = getResult { apiService.getRestaurantMenu(restaurantId) }
}