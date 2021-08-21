package com.yusufgokmenarisoy.foodorder.ui.user.order_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.Address
import com.yusufgokmenarisoy.foodorder.data.entity.PaymentType
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOrderConfirmBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderConfirmFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OrderConfirmViewModel by viewModels()
    private lateinit var binding: FragmentOrderConfirmBinding
    private var addressList = listOf<Address>()
    private var paymentTypeList = listOf<PaymentType>()
    private lateinit var addressAdapter: RecyclerRadioAdapter
    private lateinit var paymentTypeAdapter: RecyclerRadioAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapters()
        setOnClickListeners()
        setObservers()
    }

    private fun setAdapters() {
        addressAdapter = RecyclerRadioAdapter()
        binding.recyclerViewAddresses.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAddresses.adapter = addressAdapter

        paymentTypeAdapter = RecyclerRadioAdapter()
        binding.recyclerViewPaymentTypes.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPaymentTypes.adapter = paymentTypeAdapter
    }

    private fun setObservers() {
        viewModel.addresses.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.status) {
                    Resource.Status.LOADING -> binding.progressBar.show()
                    Resource.Status.SUCCESS -> {
                        if (it.data!!.success) {
                            it.data.addresses?.let { addresses ->
                                addressList = addresses
                                setAddressAdapterData()
                            }
                        } else {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.progressBar.hide()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        viewModel.paymentTypes.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.status == Resource.Status.SUCCESS) {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            paymentTypeList = it.data.paymentTypes
                            setPaymentTypeAdapterData()
                        } else {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else if(it.status == Resource.Status.ERROR) {
                    binding.progressBar.hide()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setAddressAdapterData() {
        val list = mutableListOf<RecyclerRadioItem>()
        addressList.forEach { address ->
            val id = "address_${address.id}"
            val title = "${address.title} (${address.city}/${address.district})"
            list.add(RecyclerRadioItem(id, title))
        }
        addressAdapter.setData(ArrayList(list))
    }

    private fun setPaymentTypeAdapterData() {
        val list = mutableListOf<RecyclerRadioItem>()
        paymentTypeList.forEach { paymentType ->
            val id = "type_${paymentType.id}"
            val title = paymentType.type
            list.add(RecyclerRadioItem(id, title))
        }
        paymentTypeAdapter.setData(ArrayList(list))
    }

    private fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonConfirmOrder.setOnClickListener {
            val deliveryAddressId: Int? = addressAdapter.getCheckedItemId()?.split('_')?.get(1)?.toInt()
            val paymentTypeId: Int? = paymentTypeAdapter.getCheckedItemId()?.split('_')?.get(1)?.toInt()
            if (deliveryAddressId != null && paymentTypeId != null) {
                val note = binding.editTextNote.text.toString()
                createOrder(deliveryAddressId, paymentTypeId, note)
            } else {
                Toast.makeText(context, "Lütfen teslimat adresi ve ödeme tipi seçin.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createOrder(deliveryAddressId: Int, paymentTypeId: Int, note: String) {
        viewModel.createOrder(deliveryAddressId, paymentTypeId, note).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { createResponse ->
                        if (createResponse.success) {
                            addFoodsOfOrder(createResponse.createdOrder.id)
                        } else {
                            Toast.makeText(context, createResponse.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun addFoodsOfOrder(orderId: Int) {
        viewModel.addFoodsOfOrder(orderId).observe(viewLifecycleOwner, { addResponse ->
            if (addResponse.status == Resource.Status.SUCCESS) {
                viewModel.orderReceived()
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Siparişiniz başarıyla alındı.", Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack(R.id.homeFragment, true)
                sharedViewModel.getCartItemCount()
            }
        })
    }
}