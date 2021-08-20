package com.yusufgokmenarisoy.foodorder.ui.user.food_detail

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import com.yusufgokmenarisoy.foodorder.databinding.FragmentFoodDetailBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailFragment : BaseFragment() {

    private val viewModel: FoodDetailViewModel by viewModels()
    private lateinit var binding: FragmentFoodDetailBinding
    private lateinit var food: Food
    private lateinit var dialog: Dialog
    private lateinit var dialogTitle: TextView
    private lateinit var positiveButton: MaterialButton

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

        food = viewModel.getFood()
        initViews()
        initDialog()
        setOnClickListeners()
        setObServers()
    }

    private fun initViews() {
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
            viewModel.increaseQuantity()
        }
        binding.buttonDecrease.setOnClickListener {
            viewModel.decreaseQuantity()
        }
        binding.buttonAddToCart.setOnClickListener {
            if (viewModel.addToCart()) {
                val message = if (!viewModel.isInCart()) {
                    "${food.name} sepete eklendi."
                } else {
                    "Sepetiniz güncellendi."
                }
                Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                showClearCartAndAddItemDialog()
            }
        }
        binding.buttonRemoveFromCart.setOnClickListener {
            showRemoveItemFromCartDialog()
        }
    }

    private fun setObServers() {
        viewModel.cartItem.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.itemIsInCart()
                viewModel.setQuantity(it.quantity)
                val text = "Güncelle"
                binding.buttonAddToCart.text = text
                binding.buttonRemoveFromCart.show()
            }
        })
        viewModel.quantity.observe(viewLifecycleOwner, {
            val price = "${(it * food.price)} TL"
            binding.textViewPrice.text = price
            binding.textViewQuantity.text = it.toString()
        })
        viewModel.showMaxItemWarning.observe(viewLifecycleOwner, {
            if (it) {
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Aynı üründen en fazla 10 tane ekleyebilirsiniz.", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun initDialog() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogTitle = dialog.findViewById(R.id.textViewTitle)
        positiveButton = dialog.findViewById(R.id.buttonYes)
        dialog.findViewById<MaterialButton>(R.id.buttonNo).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showRemoveItemFromCartDialog() {
        dialogTitle.text = getString(R.string.label_remove_item_from_cart)
        positiveButton.setOnClickListener {
            dialog.dismiss()
            viewModel.removeItemFromCart()
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Ürün sepetinizden çıkarıldı.", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        dialog.show()
    }

    private fun showClearCartAndAddItemDialog() {
        dialogTitle.text = getString(R.string.label_clear_cart_and_add)
        positiveButton.setOnClickListener {
            dialog.dismiss()
            viewModel.clearCartAndAddItem()
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "${food.name} sepete eklendi.", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        dialog.show()
    }
}