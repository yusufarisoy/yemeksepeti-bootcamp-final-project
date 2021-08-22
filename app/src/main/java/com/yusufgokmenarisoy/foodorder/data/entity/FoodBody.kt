package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class FoodBody(
    @SerializedName("image")
    val image: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("ingredients")
    val ingredients: String,
)
