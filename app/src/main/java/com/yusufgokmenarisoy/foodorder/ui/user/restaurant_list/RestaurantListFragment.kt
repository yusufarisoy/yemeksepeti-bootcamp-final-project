package com.yusufgokmenarisoy.foodorder.ui.user.restaurant_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentRestaurantListBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : BaseFragment() {

    private val viewModel: RestaurantListViewModel by viewModels()
    private lateinit var binding: FragmentRestaurantListBinding

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

        viewModel.setAddress(RestaurantListFragmentArgs.fromBundle(requireArguments()).address)

        initAdapters()
        initViews()
        fetchData()
    }

    private fun initAdapters() {
    }

    private fun initViews() {
        binding.textViewAddressTitle.text = viewModel.getAddress().title
        val addressDetail = "${viewModel.getAddress().city}/${viewModel.getAddress().district}"
        binding.textViewAddressDetail.text = addressDetail
    }

    private fun fetchData() {
        viewModel.getRestaurants().observe(viewLifecycleOwner, {
            if (it.status == Resource.Status.SUCCESS) {
                it.data?.let { response ->
                }
            } else if(it.status == Resource.Status.ERROR) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}