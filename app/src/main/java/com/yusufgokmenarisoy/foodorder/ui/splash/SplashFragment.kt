package com.yusufgokmenarisoy.foodorder.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentSplashBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
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

        startApp()
    }

    private fun startApp() {
        if (viewModel.isFirstLaunch()) {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment())
                viewModel.saveFirstLaunch()
            }, 500)
        } else {
            authorize()
        }
    }

    private fun authorize() {
        val token = sharedViewModel.getToken()
        if (token != null && token != "") {
            viewModel.authorizeToken(token).observe(viewLifecycleOwner, {
                if (it.status == Resource.Status.SUCCESS) {
                    if (it.data!!.success) {
                        sharedViewModel.setUser(it.data.user!!)
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                    } else {
                        Log.v("SplashFragment", "${it.data}")
                        viewModel.removeToken()
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                    }
                } else if(it.status == Resource.Status.ERROR) {
                    Log.v("SplashFragment", "${it.data}")
                    viewModel.removeToken()
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                }
            })
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            }, 500)
        }
    }
}