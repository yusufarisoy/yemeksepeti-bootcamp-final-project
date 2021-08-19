package com.yusufgokmenarisoy.foodorder.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufgokmenarisoy.foodorder.data.entity.Address
import com.yusufgokmenarisoy.foodorder.databinding.RecyclerItemAddressBinding

class AddressAdapter(private var listener: AddressOnClick) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var addressList = ArrayList<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder =
        AddressViewHolder.from(parent)

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressList[position]
        holder.bind(address, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(addressList: ArrayList<Address>) {
        if (itemCount > 0) {
            this.addressList.clear()
        }
        this.addressList = addressList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = this.addressList.size

    class AddressViewHolder(private val binding: RecyclerItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address, listener: AddressOnClick) {
            binding.textViewTitle.text = address.title
            val cityAndDistrict = "${address.city}/${address.district}"
            binding.textViewDetail.text = cityAndDistrict
            binding.itemLayout.setOnClickListener {
                listener.onClick(address)
            }
        }

        companion object {

            fun from(parent: ViewGroup): AddressViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerItemAddressBinding.inflate(layoutInflater, parent, false)
                return AddressViewHolder(binding)
            }
        }
    }
}