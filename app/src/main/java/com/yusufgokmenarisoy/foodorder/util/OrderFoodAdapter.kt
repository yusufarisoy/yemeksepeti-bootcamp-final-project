package com.yusufgokmenarisoy.foodorder.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufgokmenarisoy.foodorder.data.entity.CartItem
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemCartBinding
import com.yusufgokmenarisoy.foodorder.ui.user.cart.CartItemOnClick

class OrderFoodAdapter(private val listener: CartItemOnClick) : RecyclerView.Adapter<OrderFoodAdapter.CartItemViewHolder>() {

    private var cart = ArrayList<CartItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder =
        CartItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = cart[position]
        holder.bind(item, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(cart: ArrayList<CartItem>) {
        if (this.cart.size > 0) {
            this.cart.clear()
        }
        this.cart = cart
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = cart.size

    class CartItemViewHolder(private val binding: RecyclerItemCartBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem, listener: CartItemOnClick) {
            binding.textViewName.text = item.name
            binding.textViewQuantity.text = item.quantity.toString()
            val price = "${(item.price * item.quantity)} TL"
            binding.textViewPrice.text = price
            binding.itemLayout.setOnClickListener {
                listener.onClick(item)
            }
        }

        companion object {

            fun from(parent: ViewGroup): CartItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemCartBinding.inflate(layoutInflater, parent, false)
                return CartItemViewHolder(binding)
            }
        }
    }
}