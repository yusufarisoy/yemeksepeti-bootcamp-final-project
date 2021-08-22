package com.yusufgokmenarisoy.foodorder.ui.owner.food

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.yusufgokmenarisoy.foodorder.R
import com.yusufgokmenarisoy.foodorder.data.entity.FoodBody
import com.yusufgokmenarisoy.foodorder.data.remote.Resource
import com.yusufgokmenarisoy.foodorder.databinding.FragmentOwnerFoodBinding
import com.yusufgokmenarisoy.foodorder.ui.BaseFragment
import com.yusufgokmenarisoy.foodorder.ui.SharedViewModel
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.hide
import com.yusufgokmenarisoy.foodorder.util.Extension.Companion.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerFoodFragment : BaseFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: OwnerFoodViewModel by viewModels()
    private lateinit var binding: FragmentOwnerFoodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setOnClickListener()
    }

    private fun initViews() {
        val food = viewModel.getFood()
        if (food != null) {
            binding.editTextImage.setText(food.image)
            binding.editTextName.setText(food.name)
            val price = food.price.toString()
            binding.editTextPrice.setText(price)
            binding.editTextIngredients.setText(food.ingredients)
            binding.buttonDelete.show()
            binding.buttonDelete.setOnClickListener {
                showDialog()
            }
        }
    }

    private fun setOnClickListener() {
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonSave.setOnClickListener {
            val image = binding.editTextImage.text.toString()
            val name = binding.editTextName.text.toString()
            val price = binding.editTextPrice.text.toString()
            val ingredients = binding.editTextIngredients.text.toString()
            if (viewModel.validateInputs(image, name, price, ingredients)) {
                if (viewModel.getFood() == null) {
                    createFood(image, name, price, ingredients)
                } else {
                    updateFood(image, name, price, ingredients)
                }
            } else {
                Toast.makeText(context, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createFood(image: String, name: String, price: String, ingredients: String) {
        viewModel.createFood(sharedViewModel.getToken()!!, FoodBody(image, name, price.toInt(), ingredients)).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Ürün başarıyla eklendi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
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

    private fun updateFood(image: String, name: String, price: String, ingredients: String) {
        viewModel.updateFood(sharedViewModel.getToken()!!, FoodBody(image, name, price.toInt(), ingredients)).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Ürün başarıyla güncellendi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
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
            deleteFood()
            dialog.dismiss()
        }
        dialog.findViewById<MaterialButton>(R.id.buttonNo).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteFood() {
        viewModel.deleteFood(sharedViewModel.getToken()!!).observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.let { response ->
                        if (response.success) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Ürün başarıyla silindi.", Snackbar.LENGTH_SHORT).show()
                            findNavController().popBackStack()
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