package com.yusufgokmenarisoy.foodorder.ui.user.order_confirm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemRadioBinding

class RecyclerRadioAdapter : RecyclerView.Adapter<RecyclerRadioAdapter.RadioRecyclerViewHolder>() {

    private var itemList = ArrayList<RecyclerRadioItem>()
    private var lastCheckedPosition = -1
    private var lastChecked: MaterialRadioButton? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioRecyclerViewHolder =
        RadioRecyclerViewHolder.from(parent)

    override fun onBindViewHolder(holder: RadioRecyclerViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.radioButton.setOnClickListener {
            if (lastChecked != null) {
                lastChecked!!.isChecked = false
            }
            val selected: MaterialRadioButton = it as MaterialRadioButton
            lastChecked = selected
            selected.isChecked = true
            lastCheckedPosition = holder.adapterPosition
        }
        holder.bind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(itemList: ArrayList<RecyclerRadioItem>) {
        if (this.itemList.size > 0) {
            this.itemList.clear()
        }
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun getCheckedItemId(): String? {
        if (lastCheckedPosition != -1) {
            return itemList[lastCheckedPosition].id
        }
        return null
    }

    override fun getItemCount(): Int = itemList.size

    class RadioRecyclerViewHolder(val binding: RecyclerItemRadioBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecyclerRadioItem) {
            binding.textView.text = item.text
        }

        companion object {

            fun from(parent: ViewGroup): RadioRecyclerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemRadioBinding.inflate(layoutInflater, parent ,false)
                return RadioRecyclerViewHolder(binding)
            }
        }
    }
}