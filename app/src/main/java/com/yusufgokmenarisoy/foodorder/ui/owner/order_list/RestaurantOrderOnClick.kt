package com.yusufgokmenarisoy.foodorder.ui.owner.order_list

import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantOrder

interface RestaurantOrderOnClick {

    fun onClick(order: RestaurantOrder)
}