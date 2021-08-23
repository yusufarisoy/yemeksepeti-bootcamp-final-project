package com.yusufgokmenarisoy.foodorder.ui.user.cart

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentCartBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.OrderFoodAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: OrderFoodAdapter
    private var cart: List<CartItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setAdapter()
        setObservers()
    }

    private fun init() {
        if (viewModel.getRestaurantId() == -1) {
            binding.layoutEmptyCartWarning.show()
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setAdapter() {
        adapter = OrderFoodAdapter(object : CartItemOnClick {
            override fun onClick(cartItem: CartItem) {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToFoodDetailFragment(
                    Food(cartItem.id, viewModel.getRestaurantId(), cartItem.image, cartItem.name, cartItem.price, cartItem.ingredients)))
            }
        })
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCart.adapter = adapter
    }

    private fun setObservers() {
        if (viewModel.getRestaurantId() != -1) {
            viewModel.restaurant.observe(viewLifecycleOwner, {
                when (it.status) {
                    Resource.Status.LOADING -> binding.progressBar.show()
                    Resource.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let { response ->
                            if (response.success) {
                                binding.recyclerViewCart.show()
                                binding.layoutRestaurantDetail.show()
                                initViews(response.restaurant!!)
                                getCart()
                            }
                        }
                    }
                    Resource.Status.ERROR -> binding.progressBar.hide()
                }
            })
        }
    }

    private fun getCart() {
        viewModel.getCart()
        viewModel.cart.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                cart = it
                adapter.setData(ArrayList(it))
                var totalPrice = 0
                it.forEach { item ->
                    totalPrice += (item.quantity * item.price)
                }
                val strTotalPrice = "$totalPrice TL"
                binding.textViewTotalPrice.text = strTotalPrice
                val title = getString(R.string.title_basket) + " (${it.size})"
                binding.textViewTitle.text = title
            }
        })
    }

    private fun initViews(restaurant: Restaurant) {
        binding.buttonClearCart.show()
        binding.layoutTotalPrice.show()
        binding.buttonConfirmCart.show()
        Glide.with(requireContext()).load(restaurant.image).into(binding.imageViewRestaurant)
        binding.textViewRestaurantName.text = restaurant.name
        if (restaurant.rating != null) {
            val rate = restaurant.rating.slice(0..2)
            binding.textViewRate.text = rate

        } else {
            binding.textViewRate.text = "-"
        }
        val deliveryTime = "${restaurant.avgDeliveryTime} dk"
        binding.textViewRestaurantAvgDeliveryTimeText.text = deliveryTime
        binding.layoutRestaurantDetail.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToRestaurantDetailFragment(restaurant, true))
        }
        binding.buttonClearCart.setOnClickListener {
            showDialog()
        }
        binding.buttonConfirmCart.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderConfirmFragment(restaurant.id, cart!!.toTypedArray()))
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.findViewById<TextView>(R.id.textViewTitle).text = getString(R.string.label_clear_cart_warning)
        dialog.findViewById<MaterialButton>(R.id.buttonYes).setOnClickListener {
            viewModel.clearCart()
            findNavController().popBackStack()
            sharedViewModel.setItemCount(0)
            dialog.dismiss()
        }
        dialog.findViewById<MaterialButton>(R.id.buttonNo).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}