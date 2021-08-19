package com.yusufgokmenarisoy.foodorder.ui.user.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemPopularRestaurantBinding
import com.yusufgokmenarisoy.foodorder.util.RestaurantOnClick

class PopularRestaurantAdapter(private val listener: RestaurantOnClick) : RecyclerView.Adapter<PopularRestaurantAdapter.PopularRestaurantViewHolder>() {

    private var restaurantList = ArrayList<Restaurant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularRestaurantViewHolder =
        PopularRestaurantViewHolder.from(parent)

    override fun onBindViewHolder(holder: PopularRestaurantViewHolder, position: Int) {
        val restaurant = this.restaurantList[position]
        holder.bind(restaurant, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(restaurantList: ArrayList<Restaurant>) {
        if (itemCount > 0) {
            this.restaurantList.clear()
        }
        this.restaurantList = restaurantList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = restaurantList.size

    class PopularRestaurantViewHolder(private val binding: RecyclerItemPopularRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant, listener: RestaurantOnClick) {
            binding.textViewName.text = restaurant.name
            Glide.with(binding.root).load(restaurant.bannerImage).into(binding.imageViewBanner)
            binding.imageViewBanner.setOnClickListener {
                listener.onClick(restaurant)
            }
        }

        companion object {

            fun from(parent: ViewGroup): PopularRestaurantViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemPopularRestaurantBinding.inflate(layoutInflater, parent, false)
                return PopularRestaurantViewHolder(binding)
            }
        }
    }
}