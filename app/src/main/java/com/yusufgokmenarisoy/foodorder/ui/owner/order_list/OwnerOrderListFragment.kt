package com.yusufgokmenarisoy.foodorder.ui.owner.order_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantOrder
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOwnerOrderListBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.ui.owner.restaurant_detail.OwnerRestaurantDetailFragmentDirections
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerOrderListFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OwnerOrderListViewModel by viewModels()
    private lateinit var binding: FragmentOwnerOrderListBinding
    private lateinit var adapter: RestaurantOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        setObservers()
    }

    private fun initAdapters() {
        adapter = RestaurantOrderAdapter(object : RestaurantOrderOnClick {
            override fun onClick(order: RestaurantOrder) {
                findNavController().navigate(OwnerRestaurantDetailFragmentDirections.actionOwnerRestaurantDetailFragmentToOwnerOrderDetailFragment(order, viewModel.restaurantId))
            }
        })
        binding.recyclerViewOrderList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewOrderList.adapter = adapter
    }

    private fun setObservers() {
        viewModel.getReceivedOrders(sharedViewModel.getToken()!!)
        viewModel.orders.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            adapter.setData(ArrayList(response.orders))
                        } else {
                            binding.textViewNoOrderWarning.show()
                            binding.textViewNoOrderWarning.text = getString(R.string.label_no_active_order)
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