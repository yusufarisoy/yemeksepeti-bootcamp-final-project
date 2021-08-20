package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class AddOrderFoodBody(
    @SerializedName("food_list")
    val foodList: List<List<Any>>
)
