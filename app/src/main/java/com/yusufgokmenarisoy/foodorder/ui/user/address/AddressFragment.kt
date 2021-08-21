package com.yusufgokmenarisoy.foodorder.ui.user.address

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
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.City
import com.yusufgokmenarisoy.foodorder.data.entity.District
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentAddressBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: AddressViewModel by viewModels()
    private lateinit var binding: FragmentAddressBinding
    private lateinit var cityList: List<City>
    private lateinit var districtList: List<District>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObservers()
        setOnChangeListeners()
        setOnClickListener()
    }

    private fun initViews() {
        val address = viewModel.getAddress()
        if (address != null) {
            binding.editTextTitle.setText(address.title)
            binding.editTextAddress.setText(address.address)
            binding.textViewTitle.text = getString(R.string.title_edit_address)
            binding.buttonDelete.show()
            binding.buttonDelete.setOnClickListener {
                showDialog()
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
                            val address = viewModel.getAddress()
                            if (address != null) {
                                val index = cityList.indexOfFirst { city -> city.id == address.cityId }
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
                            val address = viewModel.getAddress()
                            if (address != null) {
                                val index = districtList.indexOfFirst { district -> district.id == address.districtId }
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

    private fun setOnClickListener() {
        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val districtId = districtList[binding.spinnerDistrict.selectedItemPosition].id
            val address = binding.editTextAddress.text.toString()
            if (viewModel.validateInputs(title, address)) {
                if (viewModel.getAddress() == null) {
                    createAddress(title, districtId, address)
                } else {
                    updateAddress(title, districtId, address)
                }
            } else {
                Toast.makeText(context, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createAddress(title: String, districtId: Int, address: String) {
        viewModel.createAddress(sharedViewModel.getToken()!!, title, districtId, address).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Adresiniz başarıyla eklendi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                            sharedViewModel.updateAddresses()
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

    private fun updateAddress(title: String, districtId: Int, address: String) {
        viewModel.updateAddress(sharedViewModel.getToken()!!, title, districtId, address).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Adres bilgileriniz başarıyla güncellendi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                            sharedViewModel.updateAddresses()
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

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.findViewById<TextView>(R.id.textViewTitle).text = getString(R.string.title_delete_address_warning)
        dialog.findViewById<MaterialButton>(R.id.buttonYes).setOnClickListener {
            deleteAddress()
            dialog.dismiss()
        }
        dialog.findViewById<MaterialButton>(R.id.buttonNo).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteAddress() {
        viewModel.deleteAddress(sharedViewModel.getToken()!!).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Adres silindi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                            sharedViewModel.updateAddresses()
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