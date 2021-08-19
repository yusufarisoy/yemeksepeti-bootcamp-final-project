package com.yusufgokmenarisoy.foodorder.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @SerializedName("id")
    val id: Int,

    @SerializedName("city_id")
    val cityId: Int,

    @SerializedName("city")
    val city: String,

    @SerializedName("district_id")
    val districtId: Int,

    @SerializedName("district")
    val district: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("last_update_date")
    val lastUpdateDate: String
) : Parcelable