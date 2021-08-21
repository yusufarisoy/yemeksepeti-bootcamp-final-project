package com.yusufgokmenarisoy.foodorder.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentLoginBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Common.Companion.validatePassword
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnChangeListeners()
        setOnClickListeners()
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
                        binding.textInputLayoutPassword.error = "Geçersiz şifre."
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })
    }

    private fun setOnClickListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            if (viewModel.validateInputs(email)) {
                login(email, password)
                binding.textInputLayoutEmail.error = null
            } else {
                showErrors("Lütfen tüm alanları doldurun.")
            }
        }

        binding.buttonToRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            viewModel.saveToken(response.token)
                            sharedViewModel.setToken(response.token)
                            sharedViewModel.setUser(response.user)
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                        } else {
                            showErrors(it.message)
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.hide()
                    showErrors(it.message)
                }
            }
        })
    }

    private fun showErrors(message: String?) {
        binding.textInputLayoutEmail.error = " "
        binding.textInputLayoutPassword.error = message
    }
}