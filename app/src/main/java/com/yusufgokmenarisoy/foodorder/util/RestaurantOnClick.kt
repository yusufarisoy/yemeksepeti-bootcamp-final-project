package com.yusufgokmenarisoy.foodorder.util

import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant

interface RestaurantOnClick {

    fun onClick(restaurant: Restaurant)
}