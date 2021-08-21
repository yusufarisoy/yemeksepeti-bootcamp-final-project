package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("restaurant_id")
    val restaurantId: Int,

    @SerializedName("score")
    val score: Int,

    @SerializedName("review")
    val review: String
)