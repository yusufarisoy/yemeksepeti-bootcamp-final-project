package com.yusufgokmenarisoy.foodorder.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOnBoarding3Binding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment3 : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOnBoarding3Binding.inflate(inflater, container, false)
        return binding.root
    }
}