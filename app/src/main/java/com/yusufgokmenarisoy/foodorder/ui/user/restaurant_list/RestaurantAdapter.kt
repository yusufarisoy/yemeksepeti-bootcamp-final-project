package com.yusufgokmenarisoy.foodorder.ui.user.restaurant_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemRestaurantBinding
import com.yusufgokmenarisoy.foodorder.util.RestaurantOnClick

class RestaurantAdapter(private val listener: RestaurantOnClick) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var restaurantList = ArrayList<Restaurant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder = RestaurantViewHolder.from(parent)

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.bind(restaurant, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(restaurantList: ArrayList<Restaurant>) {
        if (this.restaurantList.size > 0) {
            this.restaurantList.clear()
        }
        this.restaurantList = restaurantList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = restaurantList.size

    class RestaurantViewHolder(private val binding: RecyclerItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant, listener: RestaurantOnClick) {
            Glide.with(binding.root).load(restaurant.image).into(binding.imageView)
            binding.textViewName.text = restaurant.name
            binding.textViewRate.text = restaurant.rating.slice(0..2)
            val deliveryTime = "${restaurant.avgDeliveryTime}dk"
            binding.textViewAvgDeliveryTimeText.text = deliveryTime
            val minFee = "${restaurant.minOrderFee} TL"
            binding.textViewMinFee.text = minFee
            binding.itemLayout.setOnClickListener {
                listener.onClick(restaurant)
            }
        }

        companion object {

            fun from(parent: ViewGroup): RestaurantViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemRestaurantBinding.inflate(layoutInflater, parent, false)
                return RestaurantViewHolder(binding)
            }
        }
    }
}