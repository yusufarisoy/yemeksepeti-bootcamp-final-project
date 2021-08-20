package com.yusufgokmenarisoy.foodorder.ui.user.cart

import com.yusufgokmenarisoy.foodorder.data.entity.CartItem

interface CartItemOnClick {

    fun onClick(cartItem: CartItem)
}