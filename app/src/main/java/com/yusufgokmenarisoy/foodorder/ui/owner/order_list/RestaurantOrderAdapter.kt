package com.yusufgokmenarisoy.foodorder.ui.owner.order_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantOrder
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemRestaurantOrderBinding

class RestaurantOrderAdapter(private var listener: RestaurantOrderOnClick) : RecyclerView.Adapter<RestaurantOrderAdapter.RestaurantOrderViewHolder>() {

    private var orderList = ArrayList<RestaurantOrder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantOrderViewHolder =
        RestaurantOrderViewHolder.from(parent)

    override fun onBindViewHolder(holder: RestaurantOrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(orderList: ArrayList<RestaurantOrder>) {
        if (itemCount > 0) {
            this.orderList.clear()
        }
        this.orderList = orderList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = this.orderList.size

    class RestaurantOrderViewHolder(private val binding: RecyclerItemRestaurantOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: RestaurantOrder, listener: RestaurantOrderOnClick) {
            binding.textViewDate.text = order.date.slice(0..9)
            binding.textViewUser.text = order.user
            val city = "${order.city}, ${order.district}"
            binding.textViewCity.text = city
            binding.textViewAddress.text = order.deliveryAddress
            binding.textViewPaymentType.text = order.paymentType
            val price = "${order.totalPrice.toInt()} TL"
            binding.textViewPrice.text = price
            binding.itemLayout.setOnClickListener {
                listener.onClick(order)
            }
        }

        companion object {

            fun from(parent: ViewGroup): RestaurantOrderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemRestaurantOrderBinding.inflate(layoutInflater, parent, false)
                return RestaurantOrderViewHolder(binding)
            }
        }
    }
}