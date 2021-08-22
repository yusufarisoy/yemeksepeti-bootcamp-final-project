package com.yusufgokmenarisoy.foodorder.ui.owner.food_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.FoodListResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OwnerFoodListViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val restaurantId: Int = args.get<Int>("restaurantId")!!
    lateinit var foods: LiveData<Resource<FoodListResponse>>

    init {
        getFoods()
    }

    fun getRestaurantId(): Int = this.restaurantId

    private fun getFoods() {
        this.foods = apiRepository.getRestaurantMenu(this.restaurantId)
    }
}