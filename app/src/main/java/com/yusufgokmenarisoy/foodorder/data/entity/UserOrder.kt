package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class UserOrder(
    @SerializedName("id")
    val id: Int,

    @SerializedName("restaurant_id")
    val restaurantId: Int,

    @SerializedName("restaurant_image")
    val restaurantImage: String,

    @SerializedName("restaurant")
    val restaurant: String,

    @SerializedName("restaurant_score")
    val restaurantScore: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("district")
    val district: String,

    @SerializedName("delivery_address")
    val deliveryAddress: String,

    @SerializedName("payment_type")
    val paymentType: String,

    @SerializedName("order_status")
    val orderStatus: String,

    @SerializedName("note")
    val note: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("total_price")
    val totalPrice: String
)