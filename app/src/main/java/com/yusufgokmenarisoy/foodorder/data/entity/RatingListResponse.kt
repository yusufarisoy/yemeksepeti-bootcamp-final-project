package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class RatingListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val ratings: List<Rating>
)