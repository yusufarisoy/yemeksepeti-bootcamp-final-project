package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class CreatedOrder(
    @SerializedName("order_id")
    val id: Int
)
