package com.yusufgokmenarisoy.foodorder.data.remote

import com.yusufgokmenarisoy.foodorder.data.entity.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("users/login")
    suspend fun login(@Body() loginBody: LoginBody): Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body() registerBody: RegisterBody): Response<SuccessResponse>

    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserResponse>

    @GET("users/profile/addresses")
    suspend fun getAddressesOfUser(@Header("Authorization") token: String): Response<AddressResponse>

    @GET("users/profile/orders")
    suspend fun getOrderHistoryOfUser(@Header("Authorization") token: String): Response<UserOrderResponse>

    @PUT("users/profile/change-password")
    suspend fun changePassword(@Header("Authorization") token: String, @Body() changePasswordBody: ChangePasswordBody): Response<SuccessResponse>

    @PUT("users/profile/update")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body() updateProfileBody: UpdateProfileBody): Response<SuccessResponse>


    //Restaurants

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") cityId: Int): Response<RestaurantResponse>

    @GET("restaurants")
    suspend fun getRestaurantsByDistrict(@Query("d.id") districtId: Int): Response<RestaurantListResponse>

    @GET("restaurants")
    suspend fun getRestaurantsByCity(@Query("c.id") cityId: Int): Response<RestaurantListResponse>

    @GET("restaurants/most-populars/{city_id}")
    suspend fun getMostPopularRestaurants(@Path("city_id") cityId: Int): Response<RestaurantListResponse>

    @GET("restaurants/{id}/foods")
    suspend fun getRestaurantMenu(@Path("id") restaurantId: Int): Response<FoodResponse>



    //Orders

    @GET("payment-types")
    suspend fun getPaymentTypes(): Response<PaymentTypeResponse>

    @POST("orders/new")
    suspend fun createOrder(@Header("Authorization") token: String, @Body orderBody: CreateOrderBody): Response<CreateOrderResponse>

    @POST("orders/{id}/foods/add")
    suspend fun addFoodsOfOrder(@Header("Authorization") token: String, @Path("id") orderId: Int, @Body addOrderFoodBody: AddOrderFoodBody): Response<SuccessResponse>
}