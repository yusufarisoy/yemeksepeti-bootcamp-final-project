package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("id")
    val id: Int,

    @SerializedName("city_id")
    val cityId: Int,

    @SerializedName("name")
    val name: String
)