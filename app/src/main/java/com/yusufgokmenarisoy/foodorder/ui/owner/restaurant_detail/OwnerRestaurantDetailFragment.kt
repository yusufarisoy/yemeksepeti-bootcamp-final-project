package com.yusufgokmenarisoy.foodorder.ui.owner.restaurant_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOwnerRestaurantDetailBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.ui.owner.food_list.OwnerFoodListFragment
import com.yusufgokmenarisoy.foodorder.ui.owner.order_list.OwnerOrderListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerRestaurantDetailFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OwnerRestaurantDetailViewModel by viewModels()
    private lateinit var binding: FragmentOwnerRestaurantDetailBinding
    private lateinit var fragmentList: ArrayList<Fragment>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerRestaurantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObservers()
    }

    private fun setObservers() {
    }

    private fun initViews() {
        val restaurant = viewModel.getRestaurant()
        Glide.with(requireContext()).load(restaurant.image).into(binding.imageViewImage)
        Glide.with(requireContext()).load(restaurant.bannerImage).into(binding.imageViewBanner)
        val title = "${restaurant.name}, ${restaurant.district}"
        binding.textViewName.text = title
        if (restaurant.rating != null) {
            val rate = restaurant.rating.slice(0..2)
            binding.textViewRate.text = rate
        } else {
            binding.textViewRate.text = "-"
        }
        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(OwnerRestaurantDetailFragmentDirections.actionOwnerRestaurantDetailFragmentToOwnerRestaurantFragment(restaurant))
        }

        fragmentList = arrayListOf(
            initializeFragment(1),
            initializeFragment(2)
        )
        binding.viewPager.adapter = ViewPagerAdapter(activity as AppCompatActivity, fragmentList)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Siparişler"
                else -> "Menü"
            }
        }.attach()
    }

    private fun initializeFragment(i: Int): Fragment {
        val fragment = when (i) {
            1 -> OwnerOrderListFragment()
            else -> OwnerFoodListFragment()
        }
        fragment.arguments = Bundle().apply {
            putInt("restaurantId", viewModel.getRestaurant().id)
        }
        return fragment
    }
}

class ViewPagerAdapter(activity: AppCompatActivity, private val fragmentList: ArrayList<Fragment>) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}