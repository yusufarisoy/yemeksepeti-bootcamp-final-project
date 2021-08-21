package com.yusufgokmenarisoy.foodorder.ui.user.restaurant_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentRestaurantListBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.RestaurantOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: RestaurantListViewModel by viewModels()
    private lateinit var binding: FragmentRestaurantListBinding
    private lateinit var adapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initOnClickListeners()
        fetchData()
        setObservers()
    }

    private fun initViews() {
        adapter = RestaurantAdapter(object : RestaurantOnClick {
            override fun onClick(restaurant: Restaurant) {
                findNavController().navigate(RestaurantListFragmentDirections.actionRestaurantListFragmentToRestaurantDetailFragment(restaurant))
            }
        })
        binding.recyclerViewRestaurants.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewRestaurants.adapter = adapter

        val address = viewModel.getAddress()
        binding.textViewAddressTitle.text = address.title
        val addressDetail = "${address.city}/${address.district}"
        binding.textViewAddressDetail.text = addressDetail
    }

    private fun initOnClickListeners() {
        binding.buttonCart.setOnClickListener {
            findNavController().navigate(RestaurantListFragmentDirections.actionRestaurantListFragmentToCartFragment())
        }
    }

    private fun fetchData() {
        viewModel.restaurants.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.status) {
                    Resource.Status.LOADING -> binding.progressBar.show()
                    Resource.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let { response ->
                            if (response.success) {
                                adapter.setData(ArrayList(it.data.restaurants!!))
                                val countText = "${it.data.restaurants.size} restoran bulundu"
                                binding.textViewCount.text = countText
                            } else {
                                binding.textViewLabelRestaurantWarning.show()
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.progressBar.hide()
                        binding.textViewLabelRestaurantWarning.show()
                    }
                }
            }
        })
    }

    private fun setObservers() {
        sharedViewModel.cartItemCount.observe(viewLifecycleOwner, {
            if (it > 0) {
                val text = "${getString(R.string.btn_basket)} ($it)"
                binding.buttonCart.text = text
            }
        })
    }
}