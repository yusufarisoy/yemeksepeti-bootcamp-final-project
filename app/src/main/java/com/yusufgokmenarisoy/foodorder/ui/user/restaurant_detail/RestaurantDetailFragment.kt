package com.yusufgokmenarisoy.foodorder.ui.user.restaurant_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentRestaurantDetailBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment : BaseFragment() {

    private val viewModel: RestaurantDetailViewModel by viewModels()
    private lateinit var binding: FragmentRestaurantDetailBinding
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setOnClickListener()
        fetchData()
    }

    private fun initViews() {
        val restaurant = viewModel.getRestaurant()
        Glide.with(requireContext()).load(restaurant.bannerImage).into(binding.imageViewBanner)
        Glide.with(requireContext()).load(restaurant.image).into(binding.imageViewImage)
        binding.textViewName.text = restaurant.name
        binding.textViewRate.text = restaurant.rating.slice(0..2)
        val address = "${restaurant.city}/${restaurant.district}"
        binding.textViewRestaurantAddress.text = address
        val minOrder = "${restaurant.minOrderFee} TL"
        binding.textViewRestaurantMinOrderFee.text = minOrder
        val avgDelivery = "${restaurant.avgDeliveryTime} dk"
        binding.textViewRestaurantAvgDelivery.text = avgDelivery

        adapter = FoodAdapter(object : FoodOnClick {
            override fun onClick(food: Food) {
                findNavController().navigate(RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToFoodDetailFragment(food))
            }
        })
        binding.recyclerViewMenu.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMenu.adapter = adapter
    }

    private fun setOnClickListener() {
        binding.buttonCart.setOnClickListener {
            findNavController().navigate(RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToCartFragment())
        }
    }

    private fun fetchData() {
        viewModel.menu.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.status) {
                    Resource.Status.LOADING -> binding.progressBar.show()
                    Resource.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let { response ->
                            if (response.success) {
                                adapter.setData(ArrayList(response.foods!!))
                            } else {
                                binding.textViewLabelMenuWarning.show()
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.progressBar.hide()
                        binding.textViewLabelMenuWarning.show()
                    }
                }
            }
        })
    }
}