package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)