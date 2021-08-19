package com.yusufgokmenarisoy.foodorder.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOnBoardingBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var fragmentList: ArrayList<Fragment>
    private val layoutParamsActive = LinearLayout.LayoutParams(90, 20)
    private val layoutParamsInactive = LinearLayout.LayoutParams(20, 20)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initIndicators()
        initListeners()
    }

    private fun initViews() {
        fragmentList = arrayListOf(
            OnBoardingFragment1(),
            OnBoardingFragment2(),
            OnBoardingFragment3()
        )
        binding.viewPager.adapter = OnBoardingViewPagerAdapter(activity as AppCompatActivity, fragmentList)
        layoutParamsActive.marginEnd = 8
        layoutParamsInactive.marginEnd = 8
    }

    private fun initIndicators() {
        for (i in 0 until fragmentList.size) {
            val indicator = View(activity)
            when(i) {
                0 -> {
                    indicator.background = ResourcesCompat.getDrawable(resources, R.drawable.onboarding_viewpager_indicator_active, null)
                    indicator.layoutParams = layoutParamsActive
                }; else -> {
                indicator.background = ResourcesCompat.getDrawable(resources, R.drawable.onboarding_viewpager_indicator_inactive, null)
                indicator.layoutParams = layoutParamsInactive
            }
            }
            binding.viewPagerIndicator.addView(indicator)
        }
    }

    private fun initListeners() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handlePageChange(position)
                super.onPageSelected(position)
            }
        })

        binding.buttonPrevious.setOnClickListener {
            if (binding.viewPager.currentItem - 1 >= 0) {
                binding.viewPager.currentItem -= 1
            }
        }

        binding.buttonNext.setOnClickListener {
            if (binding.viewPager.currentItem + 1 < fragmentList.size) {
                binding.viewPager.currentItem += 1
            } else {
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment())
            }
        }

        binding.buttonSkip.setOnClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment())
        }
    }

    private fun handlePageChange(position: Int) {
        for (i in 0 until fragmentList.size) {
            if (i == position) {
                binding.viewPagerIndicator.getChildAt(i).layoutParams = layoutParamsActive
                binding.viewPagerIndicator.getChildAt(i).background = ResourcesCompat.getDrawable(resources, R.drawable.onboarding_viewpager_indicator_active, null)
            } else {
                binding.viewPagerIndicator.getChildAt(i).layoutParams = layoutParamsInactive
                binding.viewPagerIndicator.getChildAt(i).background = ResourcesCompat.getDrawable(resources, R.drawable.onboarding_viewpager_indicator_inactive, null)
            }
        }
        when (position) {
            0 -> binding.buttonPrevious.hide()
            fragmentList.size - 1 -> binding.buttonNext.text = getString(R.string.btn_finish)
            fragmentList.size - 2 -> {
                binding.buttonPrevious.show()
                binding.buttonNext.text = getString(R.string.btn_next)
            }
        }
    }
}