package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class UpdateAddressBody(
    @SerializedName("title")
    val title: String,

    @SerializedName("district_id")
    val districtId: Int,

    @SerializedName("address")
    val address: String
)