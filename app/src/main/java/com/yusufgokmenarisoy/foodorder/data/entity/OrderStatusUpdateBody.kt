package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class OrderStatusUpdateBody(
    @SerializedName("status_id")
    val statusId: Int
)
