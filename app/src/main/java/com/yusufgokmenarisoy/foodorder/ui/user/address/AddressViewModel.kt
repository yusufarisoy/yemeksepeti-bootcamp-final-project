package com.yusufgokmenarisoy.foodorder.ui.user.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yusufgokmenarisoy.foodorder.data.ApiRepository
import com.yusufgokmenarisoy.foodorder.data.entity.*
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    args: SavedStateHandle
) : ViewModel() {

    private val address: Address? = args.get<Address>("address")

    lateinit var cities: LiveData<Resource<CityListResponse>>

    fun getAddress(): Address? = this.address

    init {
        getCities()
    }

    private fun getCities() {
        this.cities = apiRepository.getCities()
    }

    fun getDistricts(cityId: Int): LiveData<Resource<DistrictListResponse>> = apiRepository.getDistricts(cityId)

    fun validateInputs(title: String, address: String): Boolean = title.isNotEmpty() && address.isNotEmpty()

    fun createAddress(token: String, title: String, districtId: Int, address: String): LiveData<Resource<SuccessResponse>> =
        apiRepository.createAddress(token, UpdateAddressBody(title, districtId, address))

    fun updateAddress(token: String, title: String, districtId: Int, address: String): LiveData<Resource<SuccessResponse>> =
        apiRepository.updateAddress(token, this.address!!.id, UpdateAddressBody(title, districtId, address))

    fun deleteAddress(token: String): LiveData<Resource<SuccessResponse>> = apiRepository.deleteAddress(token, this.address!!.id)
}