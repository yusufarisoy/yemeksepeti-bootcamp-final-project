package com.yusufgokmenarisoy.foodorder.ui.user.food_detail

import androidx.lifecycle.*
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import com.yusufgokmenarisoy.foodorder.data.local.CART_RESTAURANT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val food: Food = args.get<Food>("food")!!
    private var cartRestaurantId = apiRepository.getInt(CART_RESTAURANT)
    lateinit var cartItem: LiveData<CartItem?>
    private var warningShowed = false
    private var cartItemCount = 0
    private var inCart = false

    private var _quantity = MutableLiveData(1)
    val quantity: LiveData<Int>
        get() = _quantity

    private var _showMaxItemWarning = MutableLiveData(false)
    val showMaxItemWarning: LiveData<Boolean>
        get() = _showMaxItemWarning

    init {
        checkCart()
        viewModelScope.launch {
            cartItemCount = apiRepository.getItemCount()
        }
    }

    fun getFood(): Food = this.food

    fun setQuantity(quantity: Int) {
        this._quantity.value = quantity
    }

    fun isInCart(): Boolean = this.inCart

    fun itemIsInCart() {
        this.inCart = true
    }

    private fun checkCart() {
        viewModelScope.launch {
            cartItem = apiRepository.getById(food.id)
        }
    }

    fun addToCart(): Boolean {
        if (food.restaurantId == cartRestaurantId || cartRestaurantId == -1) {
            if (!inCart) {
                viewModelScope.launch {
                    _quantity.value?.let {
                        val item = CartItem(food.id, food.image, food.name, food.price, food.ingredients, it, "")
                        apiRepository.add(item)
                        if (cartRestaurantId == -1) {
                            apiRepository.saveInt(CART_RESTAURANT, food.restaurantId)
                        }
                    }
                }
            } else {
                viewModelScope.launch {
                    _quantity.value?.let {
                        val item = CartItem(food.id, food.image, food.name, food.price, food.ingredients, it, "")
                        apiRepository.update(item)
                    }
                }
            }
            return true
        }
        return false
    }

    fun increaseQuantity() {
        _quantity.value?.let {
            if (it < 10) {
                _quantity.value = it + 1
            } else if(!warningShowed) {
                warningShowed = true
                _showMaxItemWarning.value = true
            }
        }
    }

    fun decreaseQuantity() {
        _quantity.value?.let {
            if (it > 1) {
                _quantity.value = it - 1
            }
        }
    }

    fun removeItemFromCart() {
        viewModelScope.launch {
            cartItem.value?.let {
                apiRepository.delete(it)
                if (cartItemCount == 1) {
                    apiRepository.saveInt(CART_RESTAURANT, -1)
                }
            }
        }
    }

    fun clearCartAndAddItem() {
        viewModelScope.launch {
            apiRepository.clear()
        }
        cartItemCount = 0
        cartRestaurantId = -1
        addToCart()
    }
}