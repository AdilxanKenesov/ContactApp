package uz.gita.contactapp.presenter.screen.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.databinding.LoginScreenBinding
import uz.gita.contactapp.util.NotificationType
import uz.gita.contactapp.util.showNotification

class LoginScreen: Fragment(R.layout.login_screen) {
    private val binding by viewBinding(LoginScreenBinding::bind)
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtToRegister.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginScreen_to_registerScreen
            )
        }

        binding.btnLogin.setOnClickListener {
            clearErrors()

            val name = binding.etLogin.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            var hasError = false

            if (name.isEmpty()){
                binding.containerLogin.error = "Username required"
                hasError = true
            }
            if (password.isEmpty()){
                binding.containerPassword.error = "Password required"
                hasError = true
            }
            if (!hasError){
                viewModel.login(name,password)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.loginResult.observe(viewLifecycleOwner){response ->
            if (response.data != null){
                findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
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
    }
}