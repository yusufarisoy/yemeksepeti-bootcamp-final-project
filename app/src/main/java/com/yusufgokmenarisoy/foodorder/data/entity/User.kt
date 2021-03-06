package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("role")
    val role: String,

    @SerializedName("city_id")
    val cityId: Int?,

    @SerializedName("email")
    var email: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("surname")
    var surname: String,

    @SerializedName("image")
    var image: String,

    @SerializedName("phone_number")
    var phoneNumber: String
)