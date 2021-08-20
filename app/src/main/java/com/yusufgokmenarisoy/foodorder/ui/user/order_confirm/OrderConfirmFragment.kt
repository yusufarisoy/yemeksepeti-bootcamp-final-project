package com.yusufgokmenarisoy.foodorder.ui.user.order_confirm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yusufgokmenarisoy.foodorder.data.entity.CreateOrderBody
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOrderConfirmBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderConfirmFragment : BaseFragment() {

    private val viewModel: OrderConfirmViewModel by viewModels()
    private lateinit var binding: FragmentOrderConfirmBinding

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

        initViews()
        setListeners()
    }

    private fun initViews() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonConfirmOrder.setOnClickListener {
            viewModel.createOrder(2, 2, "").observe(viewLifecycleOwner, {
                when (it.status) {
                    Resource.Status.LOADING -> binding.progressBar.show()
                    Resource.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let { createResponse ->
                            if (createResponse.success) {
                                viewModel.addFoodsOfOrder(createResponse.createdOrder.id).observe(viewLifecycleOwner, { addResponse ->
                                    if (addResponse.status == Resource.Status.SUCCESS) {
                                        viewModel.orderReceived()
                                        findNavController().popBackStack()
                                    }
                                })
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
    }

    private fun setListeners() {

    }
}