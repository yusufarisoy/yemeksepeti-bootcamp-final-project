package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class CreateOrderBody(
    @SerializedName("delivery_address_id")
    val deliveryAddressId: Int,

    @SerializedName("restaurant_id")
    val restaurantId: Int,

    @SerializedName("payment_type_id")
    val paymentTypeId: Int,

    @SerializedName("note")
    val note: String
)