package com.yusufgokmenarisoy.foodorder.ui.user.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.Address
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.data.entity.UserOrder
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentHomeBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.AddressAdapter
import com.yusufgokmenarisoy.foodorder.util.AddressOnClick
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.OrderOnClick
import com.yusufgokmenarisoy.foodorder.util.RestaurantOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    //Adapters
    private lateinit var restaurantAdapter: PopularRestaurantAdapter
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var orderHistoryAdapter: HomeOrderHistoryAdapter
    private var addressArray = arrayOf<Address>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        initAdapters()
        setOnClickListeners()
        setObservers()
    }

    private fun fetchData() {
        sharedViewModel.getAddresses()
    }

    private fun initAdapters() {
        restaurantAdapter = PopularRestaurantAdapter(object : RestaurantOnClick {
            override fun onClick(restaurant: Restaurant) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRestaurantDetailFragment(restaurant))
            }
        })
        binding.recyclerViewPopularRestaurants.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopularRestaurants.adapter = restaurantAdapter

        addressAdapter = AddressAdapter(object : AddressOnClick {
            override fun onClick(address: Address) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRestaurantListFragment(address))
            }
        })
        binding.recyclerViewAddresses.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAddresses.adapter = addressAdapter

        orderHistoryAdapter = HomeOrderHistoryAdapter(object : OrderOnClick {
            override fun onClick(order: UserOrder) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOrderDetailFragment(order))
            }
        })
        binding.recyclerViewOrderHistory.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewOrderHistory.adapter = orderHistoryAdapter
    }

    private fun setOnClickListeners() {
        binding.imageButtonProfile.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
        }
        binding.buttonCart.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCartFragment())
        }
    }

    private fun setObservers() {
        sharedViewModel.user.observe(viewLifecycleOwner, {
            viewModel.getPopularRestaurants(it.cityId?: 1)
            observeRestaurants()
            val userWelcome = "Merhaba ${it.name},"
            binding.textViewWelcomeUser.text = userWelcome
            binding.imageButtonProfile.setImageResource(sharedViewModel.getUserImage())
        })

        sharedViewModel.addresses.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.status == Resource.Status.SUCCESS) {
                    it.data?.let { response ->
                        if (response.success) {
                            addressAdapter.setData(ArrayList(response.addresses!!))
                            addressArray = response.addresses.toTypedArray()
                        } else {
                            binding.textViewLabelAddressWarning.show()
                        }
                    }
                } else if(it.status == Resource.Status.ERROR) {
                    binding.textViewLabelAddressWarning.show()
                }
            }
        })

        viewModel.orders.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.status == Resource.Status.SUCCESS) {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            orderHistoryAdapter.setData(ArrayList(response.orders!!))
                        } else {
                            binding.textViewLabelOrderHistoryWarning.show()
                        }
                    }
                } else if(it.status == Resource.Status.ERROR) {
                    binding.progressBar.hide()
                    binding.textViewLabelOrderHistoryWarning.show()
                }
            }
        })

        sharedViewModel.cartItemCount.observe(viewLifecycleOwner, {
            if (it > 0) {
                val text = "${getString(R.string.btn_basket)} ($it)"
                binding.buttonCart.text = text
            }
        })

        sharedViewModel.updateOrders.observe(viewLifecycleOwner, { isUpdate ->
            if (isUpdate) {
                viewModel.getOrderHistory(sharedViewModel.getToken()!!)
                sharedViewModel.ordersUpdated()
            }
        })
    }

    private fun observeRestaurants() {
        viewModel.restaurants.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.status) {
                    Resource.Status.LOADING -> binding.progressBar.show()
                    Resource.Status.SUCCESS -> {
                        it.data?.restaurants?.let { restaurants ->
                            restaurantAdapter.setData(ArrayList(restaurants))
                        }
                    }
                    Resource.Status.ERROR -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}