package com.yusufgokmenarisoy.foodorder.ui.owner.order_detail

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
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOwnerOrderDetailBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.ui.user.cart.CartItemOnClick
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.OrderFoodAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerOrderDetailFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OwnerOrderDetailViewModel by viewModels()
    private lateinit var binding: FragmentOwnerOrderDetailBinding
    private lateinit var adapter: OrderFoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapters()
        setObservers()
    }

    private fun initViews() {
        val order = viewModel.getOrder()
        binding.textViewDate.text = order.date.slice(0..9)
        binding.textViewUser.text = order.user
        val city = "${order.city}, ${order.district}"
        binding.textViewCity.text = city
        binding.textViewAddress.text = order.deliveryAddress
        binding.textViewPaymentType.text = order.paymentType
        val price = "${order.totalPrice.toInt()} TL"
        binding.textViewPrice.text = price
        binding.textViewTotalPrice.text = price

        var newStatus = -1
        var buttonText = ""
        when (order.orderStatus) {
            "received" -> {
                newStatus = 2
                buttonText = "Onayla"
            }
            "confirmed" -> {
                newStatus = 3
                buttonText = "Sipariş Yolda"
            }
            "on the way" -> {
                newStatus = 4
                buttonText = "Teslim Edildi"
            }
            else -> {
                binding.buttonSetStatus.hide()
            }
        }

        binding.buttonSetStatus.text = buttonText
        setOnClickListeners(newStatus, buttonText)
    }

    private fun initAdapters() {
        adapter = OrderFoodAdapter(object : CartItemOnClick {
            override fun onClick(cartItem: CartItem) { }
        })
        binding.recyclerViewFoodsOfOrder.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFoodsOfOrder.adapter = adapter
    }

    private fun setObservers() {
        viewModel.getFoodListOfOrder(sharedViewModel.getToken()!!)
        viewModel.foodList.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            val list = ArrayList<CartItem>()
                            response.foods.forEach { food ->
                                list.add(CartItem(food.id, "", food.food, food.price, "", food.quantity, food.removedIngredients))
                            }
                            adapter.setData(list)
                        } else {
                            Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setOnClickListeners(newStatus: Int, message: String) {
        binding.buttonSetStatus.setOnClickListener {
            if (newStatus != -1) {
                viewModel.updateOrderStatus(sharedViewModel.getToken()!!, newStatus).observe(viewLifecycleOwner, {
                    when (it.status) {
                        Resource.Status.LOADING -> binding.progressBar.show()
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            it.data?.let { response ->
                                if (response.success) {
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
                                    findNavController().popBackStack()
                                } else {
                                    Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.hide()
                            Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }
}