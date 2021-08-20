package com.yusufgokmenarisoy.foodorder.ui.user.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantResponse
import com.yusufgokmenarisoy.foodorder.data.local.CART_RESTAURANT
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private val cartRestaurantId = apiRepository.getInt(CART_RESTAURANT)
    lateinit var cart: LiveData<List<CartItem>>
    lateinit var restaurant: LiveData<Resource<RestaurantResponse>>

    init {
        getRestaurant()
        getCart()
    }

    fun getRestaurantId() = this.cartRestaurantId

    private fun getRestaurant() {
        if (cartRestaurantId != -1) {
            restaurant = apiRepository.getRestaurantById(cartRestaurantId)
        }
    }

    fun getCart() {
        viewModelScope.launch {
            cart = apiRepository.getCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            apiRepository.clear()
        }
        apiRepository.saveInt(CART_RESTAURANT, -1)
    }
}