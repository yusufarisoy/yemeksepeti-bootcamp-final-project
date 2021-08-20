package com.yusufgokmenarisoy.foodorder.data.remote

import com.google.gson.Gson
import com.yusufgokmenarisoy.foodorder.data.entity.SuccessResponse
import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.success(body)
                }
            }
            val errorBody = Gson().fromJson(response.errorBody()?.charStream(), SuccessResponse::class.java)
            return error(errorBody.message)
        } catch (ex: Exception) {
            return error("Error: ${ex.localizedMessage}")
        }
    }

    private fun <T> error(message: String): Resource<T> = Resource.error(message)
}