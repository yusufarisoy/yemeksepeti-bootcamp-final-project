package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class RestaurantBody(
    @SerializedName("city_id")
    val cityId: Int,

    @SerializedName("district_id")
    val districtId: Int,

    @SerializedName("image")
    val image: String,

    @SerializedName("banner_image")
    val bannerImage: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("min_order_fee")
    val minOrderFee: Int,

    @SerializedName("avg_delivery_time")
    val avgDeliveryTime: String,
)