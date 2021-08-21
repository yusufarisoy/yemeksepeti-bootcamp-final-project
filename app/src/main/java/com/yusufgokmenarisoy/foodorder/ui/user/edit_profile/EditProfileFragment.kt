package com.yusufgokmenarisoy.foodorder.ui.user.edit_profile

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentEditProfileBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var binding: FragmentEditProfileBinding
    private var image: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setOnClickListeners()
    }

    private fun initViews() {
        sharedViewModel.user.observe(viewLifecycleOwner, {
            image = sharedViewModel.getUserImage()
            binding.imageView.setImageResource(image)
            binding.editTextEmail.setText(it.email)
            binding.editTextName.setText(it.name)
            binding.editTextSurname.setText(it.surname)
            binding.editTextPhoneNumber.setText(it.phoneNumber)
        })
    }
    private fun setOnClickListeners() {
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageView.setOnClickListener {
            showAvatarDialog()
        }
        binding.buttonSave.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val name = binding.editTextName.text.toString()
            val surname = binding.editTextSurname.text.toString()
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            if (viewModel.validateInputs(email, name, surname)) {
                viewModel.updateProfile(sharedViewModel.getToken()!!, email, name,  surname,
                    image, phoneNumber).observe(viewLifecycleOwner, {
                    when (it.status) {
                        Resource.Status.LOADING -> binding.progressBar.show()
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            it.data?.let { response ->
                                if (response.success) {
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content), "Bilgileriniz başarıyla güncellendi.", Snackbar.LENGTH_SHORT).show()
                                    findNavController().popBackStack()
                                    sharedViewModel.updateUser(email, name, surname, phoneNumber)
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
    }

    private fun showAvatarDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_avatar_dialog)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val image = when (checkedId) {
                R.id.avatar_1 -> R.drawable.avatar_1
                R.id.avatar_2 -> R.drawable.avatar_2
                R.id.avatar_3 -> R.drawable.avatar_3
                R.id.avatar_4 -> R.drawable.avatar_4
                R.id.avatar_5 -> R.drawable.avatar_5
                R.id.avatar_6 -> R.drawable.avatar_6
                R.id.avatar_7 -> R.drawable.avatar_7
                else -> R.drawable.avatar_8
            }
            this.image = image
            binding.imageView.setImageResource(image)
            dialog.dismiss()
        }
        dialog.show()
    }
}