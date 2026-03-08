package uz.gita.contactapp.presenter.screen.home

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.databinding.HomeScreenBinding
import uz.gita.contactapp.presenter.dialog.DialogInfoUser
import uz.gita.contactapp.presenter.dialog.DialogMessageContact
import uz.gita.contactapp.util.NotificationType
import uz.gita.contactapp.util.showNotification

class HomeScreen: Fragment(R.layout.home_screen) {
    private val binding by viewBinding(HomeScreenBinding::bind)
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMenu.setOnClickListener {
            showMenu()
        }

        observeViewModel()
    }


    private fun showMenu(){
        val wrapper = ContextThemeWrapper(requireContext(), R.style.Theme_LightPopup)
        val popupMenu = PopupMenu(wrapper, binding.btnMenu)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_my_info ->{
                    DialogInfoUser().show(parentFragmentManager, "info_dialog")
                    true
                }
                R.id.menu_logout ->{
                    val dialog = DialogMessageContact.newInstance(
                        title = "Logout",
                        message = "Are you sure you want to exit?"
                    )
                    dialog.setOnOkListener { viewModel.logout() }
                    dialog.show(parentFragmentManager, "logout_dialog")
                    true
                }
                R.id.menu_delete_account -> {
                    val dialog = DialogMessageContact.newInstance(
                        title = "Delete Account",
                        message = "Your account will be permanently deleted. Do you want to continue?"
                    )
                    dialog.setOnOkListener { viewModel.deleteAccount() }
                    dialog.show(parentFragmentManager, "delete_account_dialog")
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun observeViewModel(){
        viewModel.logoutResult.observe(viewLifecycleOwner){
            requireActivity().showNotification("Logout success", NotificationType.SUCCESS)
            navigateToLogin()
        }

        viewModel.deleteResult.observe(viewLifecycleOwner){
            requireActivity().showNotification("Delete account success", NotificationType.SUCCESS)
            navigateToLogin()
        }

        viewModel.error.observe(viewLifecycleOwner){
            requireActivity().showNotification(it, NotificationType.ERROR)
        }
    }
    private fun navigateToLogin(){
        findNavController().navigate(
            R.id.action_homeScreen_to_loginScreen,
            null,
            navOptions {
                popUpTo(R.id.homeScreen){
                    inclusive = true
                }
            }
        )
    }
}