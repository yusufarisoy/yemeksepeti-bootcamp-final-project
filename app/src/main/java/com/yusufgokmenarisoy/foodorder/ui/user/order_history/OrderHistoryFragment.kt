package com.yusufgokmenarisoy.foodorder.ui.user.order_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufgokmenarisoy.foodorder.data.entity.UserOrder
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOrderHistoryBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.OrderOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OrderHistoryViewModel by viewModels()
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        initAdapters()
        setObservers()
    }

    private fun fetchData() {
        viewModel.getOrderHistory(sharedViewModel.getToken()!!)
    }

    private fun initAdapters() {
        adapter = OrderHistoryAdapter(object : OrderOnClick {
            override fun onClick(order: UserOrder) {
                findNavController().navigate(OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderDetailFragment(order))
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun setObservers() {
        viewModel.orders.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            adapter.setData(ArrayList(response.orders!!))
                        } else {
                            binding.imageViewWarning.show()
                            binding.textViewWarning.show()
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