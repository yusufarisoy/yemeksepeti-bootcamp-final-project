package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class FoodListOfOrderResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val foods: List<OrderFood>
)