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

    suspend fun createAddress(token: String, updateAddressBody: UpdateAddressBody) = getResult { apiService.createAddress("Bearer $token", updateAddressBody) }

    suspend fun updateAddress(token: String, addressId: Int, updateAddressBody: UpdateAddressBody) = getResult { apiService.updateAddress("Bearer $token", addressId, updateAddressBody) }

    suspend fun deleteAddress(token: String, addressId: Int) = getResult { apiService.deleteAddress("Bearer $token", addressId) }

    //Restaurants

    suspend fun createRestaurant(token: String, restaurantBody: RestaurantBody) = getResult { apiService.createRestaurant("Bearer $token", restaurantBody) }

    suspend fun getRestaurantById(restaurantId: Int) = getResult { apiService.getRestaurantById(restaurantId) }

    suspend fun getRestaurants(ownerId: Int?, cityId: Int?, districtId: Int?) = getResult { apiService.getRestaurants(ownerId, cityId, districtId) }

    suspend fun getMostPopularRestaurants(cityId: Int) = getResult { apiService.getMostPopularRestaurants(cityId) }

    suspend fun getRestaurantMenu(restaurantId: Int) = getResult { apiService.getRestaurantMenu(restaurantId) }

    suspend fun getOrdersOfRestaurant(token: String, restaurantId: Int, statusId: Int?) = getResult { apiService.getOrdersOfRestaurant("Bearer $token",
        restaurantId, statusId) }

    suspend fun updateRestaurant(token: String, restaurantId: Int, restaurantBody: RestaurantBody) =
        getResult { apiService.updateRestaurant("Bearer $token", restaurantId, restaurantBody) }

    suspend fun updateOrderStatus(token: String, restaurantId: Int,orderId: Int, orderStatusUpdateBody: OrderStatusUpdateBody) =
        getResult { apiService.updateOrderStatus("Bearer $token", restaurantId, orderId, orderStatusUpdateBody) }

    suspend fun deleteRestaurant(token: String, restaurantId: Int) = getResult { apiService.deleteRestaurant("Bearer $token", restaurantId) }

    //Restaurant Foods

    suspend fun createFood(token: String, restaurantId: Int, foodBody: FoodBody) = getResult { apiService.createFood("Bearer $token", restaurantId, foodBody) }

    suspend fun updateFood(token: String, restaurantId: Int, foodId: Int, foodBody: FoodBody) = getResult {
        apiService.updateFood("Bearer $token", restaurantId, foodId, foodBody)
    }

    suspend fun deleteFood(token: String, restaurantId: Int, foodId: Int) = getResult { apiService.deleteFood("Bearer $token", restaurantId, foodId) }

    //Orders

    suspend fun getPaymentTypes() = getResult { apiService.getPaymentTypes() }

    suspend fun getFoodsOfOrder(token: String, orderId: Int) = getResult { apiService.getFoodsOfOrder("Bearer $token", orderId) }

    suspend fun createOrder(token: String, orderBody: CreateOrderBody) = getResult { apiService.createOrder("Bearer $token", orderBody) }

    suspend fun addFoodsOfOrder(token: String, orderId: Int, addOrderFoodBody: AddOrderFoodBody) =
        getResult { apiService.addFoodsOfOrder("Bearer $token", orderId, addOrderFoodBody) }

    //Ratings

    suspend fun createRating(token: String, ratingBody: RatingBody) = getResult { apiService.createRating("Bearer $token", ratingBody) }

    suspend fun getRatings(restaurantId: Int?, orderId: Int?) = getResult { apiService.getRatings(restaurantId, orderId) }

    //Cities And Districts

    suspend fun getCities() = getResult { apiService.getCities() }

    suspend fun getDistricts(cityId: Int) = getResult { apiService.getDistricts(cityId) }
}