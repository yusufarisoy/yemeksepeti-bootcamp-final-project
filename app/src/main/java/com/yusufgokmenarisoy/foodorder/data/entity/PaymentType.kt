package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class PaymentType(
    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String
)