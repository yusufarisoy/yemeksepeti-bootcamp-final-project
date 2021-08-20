package com.yusufgokmenarisoy.foodorder.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    @SerializedName("id")
    val id: Int,

    @SerializedName("city")
    val city: String,

    @SerializedName("district")
    val district: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("banner_image")
    val bannerImage: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("min_order_fee")
    val minOrderFee: Int,

    @SerializedName("avg_delivery_time")
    val avgDeliveryTime: String,

    @SerializedName("rating")
    val rating: String
) : Parcelable