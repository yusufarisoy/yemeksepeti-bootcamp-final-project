package com.yusufgokmenarisoy.foodorder.data.remote

import com.yusufgokmenarisoy.foodorder.data.entity.*
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource() {

    suspend fun login(loginBody: LoginBody) = getResult { apiService.login(loginBody) }

    suspend fun register(registerBody: RegisterBody) = getResult { apiService.register(registerBody) }

    suspend fun getUserProfile(token: String) = getResult { apiService.getUserProfile("Bearer $token") }

    suspend fun getAddressesOfUser(token: String) = getResult { apiService.getAddressesOfUser("Bearer $token") }

    suspend fun getOrderHistoryOfUser(token: String) = getResult { apiService.getOrderHistoryOfUser("Bearer $token") }

    suspend fun changePassword(token: String, changePasswordBody: ChangePasswordBody) = getResult { apiService.changePassword("Bearer $token", changePasswordBody) }

    suspend fun updateProfile(token: String, updateProfileBody: UpdateProfileBody) = getResult { apiService.updateProfile("Bearer $token", updateProfileBody) }

    //Restaurants

    suspend fun getRestaurantById(restaurantId: Int) = getResult { apiService.getRestaurantById(restaurantId) }

    suspend fun getRestaurantsByDistrict(districtId: Int) = getResult { apiService.getRestaurantsByDistrict(districtId) }

    suspend fun getRestaurantsByCity(cityId: Int) = getResult { apiService.getRestaurantsByCity(cityId) }

    suspend fun getMostPopularRestaurants(cityId: Int) = getResult { apiService.getMostPopularRestaurants(cityId) }

    suspend fun getRestaurantMenu(restaurantId: Int) = getResult { apiService.getRestaurantMenu(restaurantId) }

    //Orders

    suspend fun getPaymentTypes() = getResult { apiService.getPaymentTypes() }

    suspend fun createOrder(token: String, orderBody: CreateOrderBody) = getResult { apiService.createOrder("Bearer $token", orderBody) }

    suspend fun addFoodsOfOrder(token: String, orderId: Int, addOrderFoodBody: AddOrderFoodBody) = getResult { apiService.addFoodsOfOrder("Bearer $token", orderId, addOrderFoodBody) }
}