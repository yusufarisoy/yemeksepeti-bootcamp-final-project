package com.yusufgokmenarisoy.foodorder.util

import com.yusufgokmenarisoy.foodorder.data.entity.Food

interface FoodOnClick {

    fun onClick(food: Food)
}