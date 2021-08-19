package com.yusufgokmenarisoy.foodorder.data.entity

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("role_id")
    val roleId: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("image")
    val image: String
)