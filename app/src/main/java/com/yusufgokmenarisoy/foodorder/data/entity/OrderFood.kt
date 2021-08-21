package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class OrderFood(
    @SerializedName("id")
    val id: Int,

    @SerializedName("food")
    val food: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("removed_ingredients")
    val removedIngredients: String
)