package com.yusufgokmenarisoy.foodorder.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentSplashBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        viewModel.startApp()
    }

    private fun setObservers() {
        viewModel.isFirstLaunch().observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment())
                viewModel.saveFirstLaunch()
            } else {
                authorize()
            }
        })
    }

    private fun authorize() {
        val token = viewModel.getToken()
        if (token != null) {
            viewModel.authorizeToken(token).observe(viewLifecycleOwner, {
                when (it.status) {
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.SUCCESS -> {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment(it.data!!.user, token))
                    }
                    Resource.Status.ERROR -> {
                        Log.v("Splash", "${it.message}")
                    }
                }
            })
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }
}