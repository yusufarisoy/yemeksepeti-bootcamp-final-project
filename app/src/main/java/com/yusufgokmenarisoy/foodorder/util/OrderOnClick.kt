package com.yusufgokmenarisoy.foodorder.util

import com.yusufgokmenarisoy.foodorder.data.entity.UserOrder

interface OrderOnClick {

    fun onClick(order: UserOrder)
}