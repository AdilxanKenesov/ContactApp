package uz.gita.contactapp.presenter.screen.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.databinding.RegisterScreenBinding
import uz.gita.contactapp.util.NotificationType
import uz.gita.contactapp.util.showNotification

class RegisterScreen: Fragment(R.layout.register_screen) {
    private val binding by viewBinding(RegisterScreenBinding::bind)
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRegister.setOnClickListener {
            clearErrors()
            val name = binding.etLogin.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConPassword.text.toString().trim()

            var hasError = false

            if (name.isEmpty()) {
                binding.containerLogin.error = "Username required"
                hasError = true
            }

            if (password.isEmpty()) {
                binding.containerPassword.error = "Password required"
                hasError = true
            }

            if (confirmPassword.isEmpty()) {
                binding.containerConPassword.error = "Confirm password required"
                hasError = true
            }

            if (password != confirmPassword) {
                binding.containerConPassword.error = "Passwords do not match"
                hasError = true
            }

            if (!hasError){
                viewModel.register(name,password)
            }
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.registerResult.observe(viewLifecycleOwner){response ->
            if (response.data != null){
                findNavController().navigate(R.id.action_registerScreen_to_homeScreen)
            }else{
                requireActivity().showNotification(response.message, NotificationType.ERROR)
            }
        }
        viewModel.error.observe(viewLifecycleOwner){
            requireActivity().showNotification(it, NotificationType.ERROR)
        }
    }

    private fun clearErrors(){
        binding.containerLogin.error = null
        binding.containerPassword.error = null
        binding.containerConPassword.error = null
    }
}