package com.yusufgokmenarisoy.foodorder.data

import com.yusufgokmenarisoy.foodorder.data.entity.LoginBody
import com.yusufgokmenarisoy.foodorder.data.entity.RegisterBody
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

    fun getRestaurantsByDistrict(districtId: Int) = performNetworkOperation { remoteDataSource.getRestaurantsByDistrict(districtId) }

    fun getRestaurantsByCity(cityId: Int) = performNetworkOperation { remoteDataSource.getRestaurantsByCity(cityId) }

    fun getMostPopularRestaurants(cityId: Int) = performNetworkOperation { remoteDataSource.getMostPopularRestaurants(cityId) }



    //Local
    fun saveString(key: String, data: String) {
        this.localDataSource.saveString(key, data)
    }

    fun getString(key: String): String? = this.localDataSource.getString(key)

    fun saveBoolean(key: String, data: Boolean) {
        this.localDataSource.saveBoolean(key, data)
    }

    fun getBoolean(key: String): Boolean = this.localDataSource.getBoolean(key)
}