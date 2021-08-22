package com.yusufgokmenarisoy.foodorder.ui.owner.food_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOwnerFoodListBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.owner.restaurant_detail.OwnerRestaurantDetailFragmentDirections
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.FoodAdapter
import com.yusufgokmenarisoy.foodorder.util.FoodOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerFoodListFragment : BaseFragment() {

    private val viewModel: OwnerFoodListViewModel by viewModels()
    private lateinit var binding: FragmentOwnerFoodListBinding
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapters()
        setObservers()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(OwnerRestaurantDetailFragmentDirections.actionOwnerRestaurantDetailFragmentToOwnerFoodFragment(null, viewModel.getRestaurantId()))
        }
    }

    private fun setAdapters() {
        adapter = FoodAdapter(object : FoodOnClick {
            override fun onClick(food: Food) {
                findNavController().navigate(OwnerRestaurantDetailFragmentDirections.actionOwnerRestaurantDetailFragmentToOwnerFoodFragment(food, viewModel.getRestaurantId()))
            }
        })
        binding.recyclerViewFoodList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFoodList.adapter = adapter
    }

    private fun setObservers() {
        viewModel.foods.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            adapter.setData(ArrayList(response.foods!!))
                        } else {
                            binding.textViewNoFoodWarning.show()
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