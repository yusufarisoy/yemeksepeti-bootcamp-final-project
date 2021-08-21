package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)