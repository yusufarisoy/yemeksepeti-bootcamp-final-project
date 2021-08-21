package com.yusufgokmenarisoy.foodorder.ui.user.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.Address
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentProfileBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.MainActivity
import com.yusufgokmenarisoy.foodorder.util.AddressAdapter
import com.yusufgokmenarisoy.foodorder.util.AddressOnClick
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapters()
        setOnClickListeners()
    }

    private fun initViews() {
        val user = viewModel.getUser()
        val image = when (user.image) {
            "2" -> R.drawable.avatar_2
            "3" -> R.drawable.avatar_3
            "4" -> R.drawable.avatar_4
            "5" -> R.drawable.avatar_5
            "6" -> R.drawable.avatar_6
            "7" -> R.drawable.avatar_7
            "8" -> R.drawable.avatar_8
            else -> R.drawable.avatar_1
        }
        binding.imageView.setImageResource(image)
        val name = "${user.name} ${user.surname}"
        binding.textViewName.text = name
        binding.textViewEmail.text = user.email
    }

    private fun initAdapters() {
        addressAdapter = AddressAdapter(object : AddressOnClick {
            override fun onClick(address: Address) {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAddressFragment(address))
            }
        })
        binding.recyclerViewAddresses.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAddresses.adapter = addressAdapter

        if (viewModel.addresses.isNotEmpty()) {
            addressAdapter.setData(ArrayList(viewModel.addresses.toList()))
        }
    }

    private fun setOnClickListeners() {
        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(viewModel.getUser(), viewModel.getToken()))
        }

        binding.buttonAddAddress.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAddressFragment(null))
        }

        binding.buttonOrderHistory.setOnClickListener {

        }

        binding.buttonChangePassword.setOnClickListener {
            showPasswordDialog()
        }

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun showPasswordDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_editable_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val layoutCurrentPassword = dialog.findViewById<TextInputLayout>(R.id.textInputLayoutCurrentPassword)
        val layoutNewPassword = dialog.findViewById<TextInputLayout>(R.id.textInputLayoutNewPassword)
        val layoutNewPasswordRepeat = dialog.findViewById<TextInputLayout>(R.id.textInputLayoutNewPasswordRepeat)
        dialog.findViewById<MaterialButton>(R.id.buttonChange).setOnClickListener {
            val currentPassword = dialog.findViewById<TextInputEditText>(R.id.editTextCurrentPassword).text.toString()
            val newPassword = dialog.findViewById<TextInputEditText>(R.id.editTextNewPassword).text.toString()
            val newPasswordRepeat = dialog.findViewById<TextInputEditText>(R.id.editTextNewPasswordRepeat).text.toString()
            if (viewModel.controlPasswords(currentPassword, newPassword, newPasswordRepeat)) {
                changePassword(currentPassword, newPassword)
                dialog.dismiss()
            } else {
                layoutCurrentPassword.error = " "
                layoutNewPassword.error = " "
                layoutNewPasswordRepeat.error = " "
            }
        }
        dialog.findViewById<MaterialButton>(R.id.buttonCancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun changePassword(currentPassword: String, newPassword: String) {
        viewModel.changePassword(currentPassword, newPassword).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Şifreniz başarıyla değiştirildi.", Snackbar.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Girilen şifre yanlış.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}