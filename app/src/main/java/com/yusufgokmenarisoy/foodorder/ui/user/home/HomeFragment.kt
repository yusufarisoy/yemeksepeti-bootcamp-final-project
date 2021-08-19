package com.yusufgokmenarisoy.foodorder.ui.user.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.Address
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.data.entity.UserOrder
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentHomeBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.util.AddressAdapter
import com.yusufgokmenarisoy.foodorder.util.AddressOnClick
import com.yusufgokmenarisoy.foodorder.util.OrderOnClick
import com.yusufgokmenarisoy.foodorder.util.RestaurantOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    //Adapters
    private lateinit var restaurantAdapter: PopularRestaurantAdapter
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var orderHistoryAdapter: HomeOrderHistoryAdapter

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

        viewModel.setUser(HomeFragmentArgs.fromBundle(requireArguments()).user)
        viewModel.setToken(HomeFragmentArgs.fromBundle(requireArguments()).token)

        initAdapters()
        initViews()
        fetchData()
    }

    private fun initAdapters() {
        restaurantAdapter = PopularRestaurantAdapter(object : RestaurantOnClick {
            override fun onClick(restaurant: Restaurant) {
                viewModel.onRestaurantClicked(restaurant)
            }
        })
        binding.recyclerViewPopularRestaurants.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopularRestaurants.adapter = restaurantAdapter

        addressAdapter = AddressAdapter(object : AddressOnClick {
            override fun onClick(address: Address) {
                viewModel.onAddressClicked(address)
            }
        })
        binding.recyclerViewAddresses.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAddresses.adapter = addressAdapter

        orderHistoryAdapter = HomeOrderHistoryAdapter(object : OrderOnClick {
            override fun onClick(order: UserOrder) {
                viewModel.onOrderClicked(order)
            }
        })
        binding.recyclerViewOrderHistory.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewOrderHistory.adapter = orderHistoryAdapter
    }

    private fun initViews() {
        val userWelcome = "Merhaba ${viewModel.getUser().name},"
        binding.textViewWelcomeUser.text = userWelcome
        val image = when (viewModel.getUser().image) {
            "2" -> R.drawable.avatar_2
            "3" -> R.drawable.avatar_3
            "4" -> R.drawable.avatar_4
            "5" -> R.drawable.avatar_5
            "6" -> R.drawable.avatar_6
            "7" -> R.drawable.avatar_7
            "8" -> R.drawable.avatar_8
            else -> R.drawable.avatar_1
        }
        binding.imageButtonProfile.setImageResource(image)
        binding.nestedScrollView.isNestedScrollingEnabled = true
    }

    private fun fetchData() {
        viewModel.getPopularRestaurants(viewModel.getUser().cityId).observe(viewLifecycleOwner, {
            if (it.status == Resource.Status.SUCCESS) {
                it.data?.let { response ->
                    restaurantAdapter.setData(ArrayList(response.restaurants))
                }
            } else if(it.status == Resource.Status.ERROR) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getAddresses(viewModel.getToken()).observe(viewLifecycleOwner, {
            if (it.status == Resource.Status.SUCCESS) {
                it.data?.let { response ->
                    addressAdapter.setData(ArrayList(response.addresses))
                }
            } else if(it.status == Resource.Status.ERROR) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getOrderHistory(viewModel.getToken()).observe(viewLifecycleOwner, {
            if (it.status == Resource.Status.SUCCESS) {
                it.data?.let { response ->
                    orderHistoryAdapter.setData(ArrayList(response.orders))
                }
            } else if(it.status == Resource.Status.ERROR) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}