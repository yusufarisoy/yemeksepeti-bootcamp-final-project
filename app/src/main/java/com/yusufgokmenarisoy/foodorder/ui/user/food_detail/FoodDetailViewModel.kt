package com.yusufgokmenarisoy.foodorder.ui.user.food_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val food: Food = args.get<Food>("food")!!
    private var quantity = 1
    private var warningShowed = false

    fun getFood(): Food = this.food

    fun getQuantity(): Int = this.quantity

    fun isWarningShowed(): Boolean = this.warningShowed

    fun warningShowed() {
        this.warningShowed = true
    }

    fun addToCart() {
        //TODO
    }

    fun increaseQuantity(): Boolean {
        if (quantity < 15) {
            quantity++
            return true
        }
        return false
    }

    fun decreaseQuantity(): Boolean {
        if (quantity > 1) {
            quantity--
            return true
        }
        return false
    }
}