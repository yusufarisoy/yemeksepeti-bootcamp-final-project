package com.yusufgokmenarisoy.foodorder.data

import androidx.lifecycle.LiveData
import com.yusufgokmenarisoy.foodorder.data.entity.*
import com.yusufgokmenarisoy.foodorder.data.local.LocalDataSource
import com.yusufgokmenarisoy.foodorder.data.remote.RemoteDataSource
import com.yusufgokmenarisoy.foodorder.data.remote.performNetworkOperation
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    fun login(loginBody: LoginBody) = performNetworkOperation { remoteDataSource.login(loginBody) }

    fun register(registerBody: RegisterBody) = performNetworkOperation { remoteDataSource.register(registerBody) }

    fun getUserProfile(token: String) = performNetworkOperation { remoteDataSource.getUserProfile(token) }

    fun getAddressesOfUser(token: String) = performNetworkOperation { remoteDataSource.getAddressesOfUser(token) }

    fun getOrderHistoryOfUser(token: String) = performNetworkOperation { remoteDataSource.getOrderHistoryOfUser(token) }

    //Restaurants

    fun getRestaurantById(restaurantId: Int) = performNetworkOperation { remoteDataSource.getRestaurantById(restaurantId) }

    fun getRestaurantsByDistrict(districtId: Int) = performNetworkOperation { remoteDataSource.getRestaurantsByDistrict(districtId) }

    fun getRestaurantsByCity(cityId: Int) = performNetworkOperation { remoteDataSource.getRestaurantsByCity(cityId) }

    fun getMostPopularRestaurants(cityId: Int) = performNetworkOperation { remoteDataSource.getMostPopularRestaurants(cityId) }

    fun getRestaurantMenu(restaurantId: Int) = performNetworkOperation { remoteDataSource.getRestaurantMenu(restaurantId) }

    //Orders

    fun createOrder(token: String, orderBody: CreateOrderBody) = performNetworkOperation { remoteDataSource.createOrder(token, orderBody) }

    fun addFoodsOfOrder(token: String, orderId: Int, addOrderFoodBody: AddOrderFoodBody) = performNetworkOperation { remoteDataSource.addFoodsOfOrder(token, orderId, addOrderFoodBody) }



    //Local
    fun saveString(key: String, data: String) {
        this.localDataSource.saveString(key, data)
    }

    fun getString(key: String): String? = this.localDataSource.getString(key)

    fun saveInt(key: String, data: Int) {
        this.localDataSource.saveInt(key, data)
    }

    fun getInt(key: String) = this.localDataSource.getInt(key)

    fun saveBoolean(key: String, data: Boolean) {
        this.localDataSource.saveBoolean(key, data)
    }

    fun getBoolean(key: String): Boolean = this.localDataSource.getBoolean(key)

    //Database
    suspend fun add(cartItem: CartItem) {
        this.localDataSource.add(cartItem)
    }

    fun getCart(): LiveData<List<CartItem>> = this.localDataSource.getCart()

    suspend fun getItemCount(): Int = this.localDataSource.getItemCount()

    fun getById(id: Int): LiveData<CartItem?> = this.localDataSource.getById(id)

    suspend fun update(cartItem: CartItem) {
        this.localDataSource.update(cartItem)
    }

    suspend fun delete(cartItem: CartItem) {
        this.localDataSource.delete(cartItem)
    }

    suspend fun clear() {
        this.localDataSource.clear()
    }
}