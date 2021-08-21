package com.yusufgokmenarisoy.foodorder.ui.user.order_history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusufgokmenarisoy.foodorder.data.entity.UserOrder
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemOrderHistoryBinding
import com.yusufgokmenarisoy.foodorder.util.OrderOnClick

class OrderHistoryAdapter(private val listener: OrderOnClick) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

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

    class OrderViewHolder(private val binding: RecyclerItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: UserOrder, listener: OrderOnClick) {
            Glide.with(binding.root).load(order.restaurantImage).into(binding.imageView)
            binding.textViewDate.text = order.date.slice(0..2)
            binding.textViewName.text = order.restaurant
            val address = "${order.city}, ${order.district}"
            binding.textViewAddress.text = address
            binding.textViewRate.text = order.restaurantScore.slice(0..2)
            binding.textViewStatus.text = order.orderStatus
            binding.textViewPaymentType.text = order.paymentType
            val price = "${order.totalPrice} TL"
            binding.textViewPrice.text = price
            binding.itemLayout.setOnClickListener {
                listener.onClick(order)
            }
        }

        companion object {

            fun from(parent: ViewGroup): OrderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemOrderHistoryBinding.inflate(layoutInflater, parent, false)
                return OrderViewHolder(binding)
            }
        }
    }
}