package com.yusufgokmenarisoy.foodorder.ui.user.order_confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.*
import com.yusufgokmenarisoy.foodorder.data.local.CART_RESTAURANT
import com.yusufgokmenarisoy.foodorder.data.local.TOKEN
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderConfirmViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    lateinit var addresses: LiveData<Resource<AddressResponse>>
    lateinit var paymentTypes: LiveData<Resource<PaymentTypeResponse>>
    private val restaurantId = args.get<Int>("restaurantId")
    private val cart = args.get<Array<CartItem>>("cart")
    private val token = apiRepository.getString(TOKEN)

    init {
        getAddresses(token!!)
        getPaymentTypes()
    }

    private fun getAddresses(token: String) {
        this.addresses = apiRepository.getAddressesOfUser(token)
    }

    private fun getPaymentTypes() {
        this.paymentTypes = apiRepository.getPaymentTypes()
    }

    fun createOrder(deliveryAddressId: Int, paymentTypeId: Int, note: String): LiveData<Resource<CreateOrderResponse>> =
        apiRepository.createOrder(token!!, CreateOrderBody(deliveryAddressId, restaurantId!!, paymentTypeId, note))

    fun addFoodsOfOrder(orderId: Int): LiveData<Resource<SuccessResponse>> {
        val body = mutableListOf<List<Any>>()
        for (i in cart!!.indices) {
            body.add(i, listOf(orderId, cart[i].id, cart[i].quantity, cart[i].removedIngredients))
        }
        return apiRepository.addFoodsOfOrder(token!!, orderId, AddOrderFoodBody(body))
    }

    fun orderReceived() {
        viewModelScope.launch {
            apiRepository.clear()
        }
        apiRepository.saveInt(CART_RESTAURANT, -1)
    }
}