package com.yusufgokmenarisoy.foodorder.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentRegisterBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.util.Common.Companion.validatePassword
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnChangeListeners()
        setOnClickListener()
    }

    private fun setOnChangeListeners() {
        binding.editTextPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.apply {
                    if (validatePassword() && toString().length > 4) {
                        viewModel.isPasswordValid = true
                        binding.textInputLayoutPassword.error = null
                    } else {
                        viewModel.isPasswordValid = false
                        binding.textInputLayoutPassword.error = " "
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })
    }

    private fun setOnClickListener() {
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val name = binding.editTextName.text.toString().trim()
            val surname = binding.editTextSurname.text.toString().trim()
            val role = if (binding.checkboxRole.isChecked) 2 else 1
            if (viewModel.validateInputs(email, name, surname)) {
                register(role, email, password, name, surname)
                binding.editTextPassword.error = null
            } else {
                binding.textInputLayoutEmail.error = " "
                binding.textInputLayoutPassword.error = " "
                binding.textInputLayoutName.error = " "
                binding.textInputLayoutSurname.error = "Lütfen tüm alanları doldurun."
            }
        }
    }

    private fun register(role: Int, email: String, password: String, name: String, surname: String) {
        viewModel.register(role, email, password, name, surname).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        "Kayıt başarıyla gerçekleştirildi.", Snackbar.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    binding.textInputLayoutEmail.error = " "
                    binding.textInputLayoutPassword.error = " "
                    binding.textInputLayoutName.error = " "
                    binding.textInputLayoutSurname.error = it.message
                }
            }
        })
    }
}