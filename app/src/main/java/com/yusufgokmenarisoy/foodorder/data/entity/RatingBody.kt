package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class RatingBody(
    @SerializedName("restaurant_id")
    val restaurantId: Int,

    @SerializedName("order_id")
    val orderId: Int,

    @SerializedName("score")
    val score: Int,

    @SerializedName("review")
    val review: String
)