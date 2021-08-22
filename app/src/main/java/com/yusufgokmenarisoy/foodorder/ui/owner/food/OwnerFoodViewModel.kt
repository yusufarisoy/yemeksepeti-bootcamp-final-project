package com.yusufgokmenarisoy.foodorder.ui.owner.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import com.yusufgokmenarisoy.foodorder.data.entity.FoodBody
import com.yusufgokmenarisoy.foodorder.data.entity.SuccessResponse
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class OwnerFoodViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val food: Food? = args.get<Food>("food")
    private val restaurantId: Int = args.get<Int>("restaurantId")!!

    fun getFood(): Food? = this.food

    fun validateInputs(image: String, name: String, price: String, ingredients: String): Boolean {
        return try {
            image.isNotEmpty() && name.isNotEmpty() && price.isNotEmpty() && price.toInt() > 0 && ingredients.isNotEmpty()
        } catch (ex: NumberFormatException) {
            false
        }
    }

    fun createFood(token: String, foodBody: FoodBody): LiveData<Resource<SuccessResponse>> =
        this.apiRepository.createFood(token, this.restaurantId, foodBody)

    fun updateFood(token: String, foodBody: FoodBody): LiveData<Resource<SuccessResponse>> =
        this.apiRepository.updateFood(token, this.restaurantId, this.food!!.id, foodBody)

    fun deleteFood(token: String): LiveData<Resource<SuccessResponse>> =
        this.apiRepository.deleteFood(token, this.restaurantId, this.food!!.id)
}