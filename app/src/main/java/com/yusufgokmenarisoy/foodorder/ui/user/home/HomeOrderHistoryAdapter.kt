package com.yusufgokmenarisoy.foodorder.ui.user.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusufgokmenarisoy.foodorder.data.entity.UserOrder
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemOrderHistoryHomeBinding
import com.yusufgokmenarisoy.foodorder.util.OrderOnClick

class HomeOrderHistoryAdapter(private val listener: OrderOnClick) : RecyclerView.Adapter<HomeOrderHistoryAdapter.OrderViewHolder>() {

    private var orderList = ArrayList<UserOrder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder = OrderViewHolder.from(parent)

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(orderList: ArrayList<UserOrder>) {
        if (this.orderList.size > 0) {
            this.orderList.clear()
        }
        this.orderList = orderList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = orderList.size

    class OrderViewHolder(private val binding: RecyclerItemOrderHistoryHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: UserOrder, listener: OrderOnClick) {
            Glide.with(binding.root).load(order.restaurantImage).into(binding.imageView)
            binding.textViewDate.text = order.date.slice(0..9)
            binding.textViewName.text = order.restaurant
            val address = "${order.city}/${order.district}"
            binding.textViewAddress.text = address
            val totalPrice = "${order.totalPrice.toInt()} TL"
            binding.textViewTotalPrice.text = totalPrice
            binding.itemLayout.setOnClickListener {
                listener.onClick(order)
            }
        }

        companion object {

            fun from(parent: ViewGroup): OrderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemOrderHistoryHomeBinding.inflate(layoutInflater, parent, false)
                return OrderViewHolder(binding)
            }
        }
    }
}