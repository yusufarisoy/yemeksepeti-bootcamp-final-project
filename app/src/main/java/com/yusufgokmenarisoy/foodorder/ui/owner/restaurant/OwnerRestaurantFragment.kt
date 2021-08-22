package com.yusufgokmenarisoy.foodorder.ui.owner.restaurant

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.City
import com.yusufgokmenarisoy.foodorder.data.entity.District
import com.yusufgokmenarisoy.foodorder.data.entity.RestaurantBody
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOwnerRestaurantBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerRestaurantFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OwnerRestaurantViewModel by viewModels()
    private lateinit var binding: FragmentOwnerRestaurantBinding
    private lateinit var cityList: List<City>
    private lateinit var districtList: List<District>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObservers()
        setOnChangeListeners()
        setOnClickListeners()
    }

    private fun initViews() {
        val restaurant = viewModel.getRestaurant()
        if (restaurant != null) {
            Glide.with(requireContext()).load(restaurant.image).into(binding.imageView)
            Glide.with(requireContext()).load(restaurant.bannerImage).into(binding.imageViewBanner)
            binding.textViewTitle.text = getText(R.string.title_add_restaurant)
            binding.editTextImage.setText(restaurant.image)
            binding.editTextBannerImage.setText(restaurant.bannerImage)
            binding.editTextName.setText(restaurant.name)
            binding.editTextPhoneNumber.setText(restaurant.phoneNumber)
            binding.editTextMinOrderFee.setText("${restaurant.minOrderFee}")
            binding.editTextAvgDeliveryTime.setText(restaurant.avgDeliveryTime)
            binding.buttonDelete.show()
            binding.buttonDelete.setOnClickListener {
                showDialog(restaurant.id)
            }
        }
    }

    private fun setObservers() {
        viewModel.cities.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    it.data?.let { response ->
                        binding.progressBar.hide()
                        if (response.success) {
                            cityList = response.cities
                            val list = ArrayList<String>()
                            cityList.forEach { city ->
                                list.add(city.name)
                            }
                            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, list)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerCity.adapter = adapter
                            val restaurant = viewModel.getRestaurant()
                            if (restaurant != null) {
                                val index = cityList.indexOfFirst { city -> city.id == restaurant.cityId }
                                binding.spinnerCity.setSelection(index)
                                val cityId = cityList[binding.spinnerCity.selectedItemPosition].id
                                getDistricts(cityId)
                            }
                        } else {
                            binding.progressBar.hide()
                            Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getDistricts(cityId: Int) {
        viewModel.getDistricts(cityId).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.spinnerDistrict.show()
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            districtList = response.districts
                            val list = ArrayList<String>()
                            districtList.forEach { district ->
                                list.add(district.name)
                            }
                            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, list)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerDistrict.adapter = adapter
                            val restaurant = viewModel.getRestaurant()
                            if (restaurant != null) {
                                val index = districtList.indexOfFirst { district -> district.id == restaurant.districtId }
                                binding.spinnerDistrict.setSelection(index)
                            }
                        } else {
                            Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setOnChangeListeners() {
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val cityId = cityList[position].id
                getDistricts(cityId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun setOnClickListeners() {
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonPreviewImage.setOnClickListener {
            val image = binding.editTextImage.text.toString()
            if (image.isNotEmpty()) {
                Glide.with(requireContext()).load(image).into(binding.imageView)
            }
        }
        binding.buttonPreviewBannerImage.setOnClickListener {
            val image = binding.editTextBannerImage.text.toString()
            if (image.isNotEmpty()) {
                Glide.with(requireContext()).load(image).into(binding.imageViewBanner)
            }
        }
        binding.buttonSave.setOnClickListener {
            val bannerImage = binding.editTextBannerImage.text.toString()
            val image = binding.editTextImage.text.toString()
            val name = binding.editTextName.text.toString()
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val minOrderFee = binding.editTextMinOrderFee.text.toString()
            val avgDeliveryTime = binding.editTextAvgDeliveryTime.text.toString()
            if (viewModel.validateInputs(bannerImage, image, name, phoneNumber, minOrderFee, avgDeliveryTime)) {
                val cityId = cityList[binding.spinnerCity.selectedItemPosition].id
                val districtId = districtList[binding.spinnerDistrict.selectedItemPosition].id
                if (viewModel.getRestaurant() == null) {
                    createRestaurant(cityId, districtId, image, bannerImage, name, phoneNumber, minOrderFee.toInt(), avgDeliveryTime)
                } else {
                    updateRestaurant(viewModel.getRestaurant()!!.id, cityId, districtId, image, bannerImage, name, phoneNumber, minOrderFee.toInt(), avgDeliveryTime)
                }
            }
        }
    }

    private fun createRestaurant(cityId: Int, districtInt: Int, image: String, bannerImage: String, name: String, phoneNumber: String,
                                 minOrderFee: Int, avgDeliveryTime: String) {
        viewModel.createRestaurant(sharedViewModel.getToken()!!, RestaurantBody(cityId, districtInt, image, bannerImage, name, phoneNumber,
            minOrderFee, avgDeliveryTime)).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Restoran başarıyla eklendi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                            sharedViewModel.restaurantsChanged()
                        } else {
                            Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateRestaurant(restaurantId: Int, cityId: Int, districtInt: Int, image: String, bannerImage: String, name: String,
                                 phoneNumber: String, minOrderFee: Int, avgDeliveryTime: String) {
        viewModel.updateRestaurant(sharedViewModel.getToken()!!, restaurantId, RestaurantBody(cityId, districtInt, image, bannerImage,
            name, phoneNumber, minOrderFee, avgDeliveryTime)).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Restoran bilgileri başarıyla güncellendi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack(R.id.ownerHomeFragment, false)
                            sharedViewModel.restaurantsChanged()
                        } else {
                            Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showDialog(restaurantId: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.findViewById<TextView>(R.id.textViewTitle).text = getString(R.string.title_delete_address_warning)
        dialog.findViewById<MaterialButton>(R.id.buttonYes).setOnClickListener {
            deleteRestaurant(restaurantId)
            dialog.dismiss()
        }
        dialog.findViewById<MaterialButton>(R.id.buttonNo).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteRestaurant(restaurantId: Int) {
        viewModel.deleteRestaurant(sharedViewModel.getToken()!!, restaurantId).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Restoran silindi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack(R.id.ownerHomeFragment, false)
                            sharedViewModel.restaurantsChanged()
                        } else {
                            Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, "Bir hata meydana geldi: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}