package com.yusufgokmenarisoy.foodorder.ui.owner.home

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.Restaurant
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOwnerHomeBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import com.yusufgokmenarisoy.foodorder.util.RestaurantAdapter
import com.yusufgokmenarisoy.foodorder.util.RestaurantOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerHomeFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OwnerHomeViewModel by viewModels()
    private lateinit var binding: FragmentOwnerHomeBinding
    private lateinit var adapter: RestaurantAdapter
    private var ownerId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        initViews()
        setOnClickListeners()
    }

    private fun fetchData() {
        sharedViewModel.user.observe(viewLifecycleOwner, {
            ownerId = it.id
            viewModel.getRestaurantsByOwnerId(it.id)
            setObservers()
        })
    }

    private fun initViews() {
        adapter = RestaurantAdapter(false, object : RestaurantOnClick {
            override fun onClick(restaurant: Restaurant) {
                findNavController().navigate(OwnerHomeFragmentDirections.actionOwnerHomeFragmentToOwnerRestaurantDetailFragment(restaurant))
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun setObservers() {
        viewModel.restaurants.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            adapter.setData(ArrayList(it.data.restaurants!!))
                        } else {
                            showWarnings()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    showWarnings()
                }
            }
        })
        sharedViewModel.updateRestaurants.observe(viewLifecycleOwner, { isUpdate ->
            if (isUpdate && ownerId != null) {
                viewModel.updateRestaurants(ownerId!!)
                sharedViewModel.restaurantsUpdated()
            }
        })
    }

    private fun showWarnings() {
        binding.imageViewWarning.show()
        binding.textViewWarning.show()
        binding.buttonAddRestaurant.show()
    }

    private fun setOnClickListeners() {
        binding.buttonSettings.setOnClickListener {
            showSettingsDialog()
        }
    }

    private fun showSettingsDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_settings_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.findViewById<MaterialButton>(R.id.buttonProfile).setOnClickListener {
            findNavController().navigate(OwnerHomeFragmentDirections.actionOwnerHomeFragmentToProfileFragment())
            dialog.dismiss()
        }
        dialog.findViewById<MaterialButton>(R.id.buttonAddRestaurant).setOnClickListener {
            findNavController().navigate(OwnerHomeFragmentDirections.actionOwnerHomeFragmentToOwnerRestaurantFragment(null))
            dialog.dismiss()
        }
        dialog.findViewById<MaterialButton>(R.id.buttonCancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}