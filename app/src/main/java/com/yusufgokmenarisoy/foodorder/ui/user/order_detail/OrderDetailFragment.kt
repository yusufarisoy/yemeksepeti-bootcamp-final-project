package com.yusufgokmenarisoy.foodorder.ui.user.order_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem
import com.yusufgokmenarisoy.foodorder.data.entity.Rating
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOrderDetailBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.ui.user.cart.CartItemOnClick
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.OrderFoodAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OrderDetailViewModel by viewModels()
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var adapter: OrderFoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        initViews()
        fetchData()
    }

    private fun initAdapters() {
        adapter = OrderFoodAdapter(object : CartItemOnClick {
            override fun onClick(cartItem: CartItem) { }
        })
        binding.recyclerViewFoodsOfOrder.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFoodsOfOrder.adapter = adapter
    }

    private fun initViews() {
        val order = viewModel.getOrder()
        Glide.with(requireContext()).load(order.restaurantImage).into(binding.imageView)
        binding.textViewName.text = order.restaurant
        val address = "${order.city}, ${order.district}"
        binding.textViewAddress.text = address
        if (order.restaurantScore != null) {
            binding.textViewRate.text = order.restaurantScore.slice(0..2)
        } else {
            binding.textViewRate.text = "-"
        }
        val price = "${order.totalPrice.toInt() / 2} TL"
        binding.textViewTotalPrice.text = price
    }

    private fun fetchData() {
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

        viewModel.getRating().observe(viewLifecycleOwner, {
            if (it.status == Resource.Status.SUCCESS) {
                it.data?.let { response ->
                    if (response.success) {
                        val rating = response.ratings[0]
                        orderRated(rating)
                    } else {
                        orderIsNotRated()
                    }
                }
            }
        })
    }

    private fun orderRated(rating: Rating) {
        when (rating.score) {
            1 -> binding.score1.isChecked = true
            2 -> binding.score2.isChecked = true
            3 -> binding.score3.isChecked = true
            4 -> binding.score4.isChecked = true
            else -> binding.score5.isChecked = true
        }
        binding.radioGroup.children.forEach {
            it.isClickable = false
        }
        binding.editTextReview.setText(rating.review)
        binding.buttonRate.hide()
        binding.editTextReview.isEnabled = false
    }

    private fun orderIsNotRated() {
        binding.buttonRate.setOnClickListener {
            if (binding.radioGroup.checkedRadioButtonId != -1) {
                val score = when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.score1 -> 1
                    R.id.score2 -> 2
                    R.id.score3 -> 3
                    R.id.score4 -> 4
                    else -> 5
                }
                viewModel.createRating(sharedViewModel.getToken()!!, score, binding.editTextReview.text.toString()).observe(viewLifecycleOwner, {
                    when (it.status) {
                        Resource.Status.LOADING -> binding.progressBar.show()
                        Resource.Status.SUCCESS -> {
                            it.data?.let { response ->
                                if (response.success) {
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content), "Değerlendirmeniz alındı.", Snackbar.LENGTH_SHORT).show()
                                    findNavController().popBackStack()
                                } else {
                                    binding.progressBar.hide()
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
            } else {
                Toast.makeText(context, "Puan kısmını boş bırakmayın.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}