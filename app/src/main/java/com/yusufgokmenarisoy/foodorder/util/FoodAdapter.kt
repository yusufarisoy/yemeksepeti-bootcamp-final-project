package com.yusufgokmenarisoy.foodorder.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusufgokmenarisoy.foodorder.data.entity.Food
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemFoodBinding

class FoodAdapter(private val listener: FoodOnClick) : RecyclerView.Adapter<FoodAdapter.MenuViewHolder>() {

    private var menu = ArrayList<Food>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder =
        MenuViewHolder.from(parent)

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val food = menu[position]
        holder.bind(food, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(menu: ArrayList<Food>) {
        if (this.menu.size > 0) {
            this.menu.clear()
        }
        this.menu = menu
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = menu.size

    class MenuViewHolder(private val binding: RecyclerItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food, listener: FoodOnClick) {
            Glide.with(binding.root).load(food.image).into(binding.imageViewFood)
            binding.textViewFoodName.text = food.name
            binding.textViewFoodIngredients.text = food.ingredients
            val price = "${food.price} TL"
            binding.textViewFoodPrice.text = price
            binding.itemLayout.setOnClickListener {
                listener.onClick(food)
            }
        }

        companion object {

            fun from(parent: ViewGroup): MenuViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemFoodBinding.inflate(layoutInflater, parent, false)
                return MenuViewHolder(binding)
            }
        }
    }
}