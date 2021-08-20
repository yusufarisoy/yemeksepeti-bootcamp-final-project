package com.yusufgokmenarisoy.foodorder.ui.user.food_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.databinding.FragmentFoodDetailBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailFragment : BaseFragment() {

    private val viewModel: FoodDetailViewModel by viewModels()
    private lateinit var binding: FragmentFoodDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setOnClickListeners()
    }

    private fun initViews() {
        val food = viewModel.getFood()
        Glide.with(requireContext()).load(food.image).into(binding.imageView)
        binding.textViewName.text = food.name
        val price = "${food.price} TL"
        binding.textViewPrice.text = price
        binding.textViewIngredients.text = food.ingredients
    }

    private fun setOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonIncrease.setOnClickListener {
            if (viewModel.increaseQuantity()) {
                binding.textViewQuantity.text = viewModel.getQuantity().toString()
            } else if(!viewModel.isWarningShowed()) {
                viewModel.warningShowed()
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Aynı üründen en fazla 15 tane ekleyebilirsiniz.", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.buttonDecrease.setOnClickListener {
            if (viewModel.decreaseQuantity()) {
                binding.textViewQuantity.text = viewModel.getQuantity().toString()
            }
        }
        binding.buttonAddToCart.setOnClickListener {
            viewModel.addToCart()
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "${viewModel.getFood().name} sepete eklendi.", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}