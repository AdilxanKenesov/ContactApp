package uz.gita.contactapp.presenter.screen.home

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.databinding.HomeScreenBinding
import uz.gita.contactapp.presenter.adapter.ContactAdapter
import uz.gita.contactapp.presenter.dialog.DialogAddContact
import uz.gita.contactapp.presenter.dialog.DialogDeleteContact
import uz.gita.contactapp.presenter.dialog.DialogInfoUser
import uz.gita.contactapp.presenter.dialog.DialogMessageContact
import uz.gita.contactapp.presenter.dialog.DialogUpdateContact
import uz.gita.contactapp.util.NotificationType
import uz.gita.contactapp.util.showNotification

class HomeScreen: Fragment(R.layout.home_screen) {
    private val binding by viewBinding(HomeScreenBinding::bind)
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }
    private val adapter by lazy { ContactAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMenu.setOnClickListener {
            showMenu()
        }

        observeViewModel()
        addContact()

        binding.recyclerView.adapter = adapter
        setupSwipeGestures()

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

    private fun addContact(){
        binding.btnAddContact.setOnClickListener {
            val dialog = DialogAddContact()
            dialog.setListener {
                viewModel.addContact(it.name, it.phone)
            }
            dialog.show(parentFragmentManager, "add_contact_dialog")
        }
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
        viewModel.getContacts.observe(viewLifecycleOwner){ result->
            val list = result.data
            if (list.isNullOrEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.lottieView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.lottieView.visibility = View.GONE
                adapter.submitList(list)
            }
        }
        viewModel.addContact.observe(viewLifecycleOwner){
            requireActivity().showNotification("Contact added", NotificationType.SUCCESS)
            viewModel.loadContact()
        }
        viewModel.deleteContactResult.observe(viewLifecycleOwner){
            requireActivity().showNotification("Contact delete", NotificationType.SUCCESS)
            viewModel.loadContact()
        }
        viewModel.updateContactResult.observe(viewLifecycleOwner){
            requireActivity().showNotification("Contact update", NotificationType.SUCCESS)
            viewModel.loadContact()
        }

    }

    private fun setupSwipeGestures() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val contact = adapter.currentList[position]

                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        val dialog = DialogDeleteContact.newInstance(contact.name)
                        dialog.setOnDeleteListener {
                            viewModel.deleteContact(contact.id.toInt())
                        }
                        dialog.show(parentFragmentManager, "delete_dialog")
                    }

                    ItemTouchHelper.LEFT -> {
                        val dialog = DialogUpdateContact.newInstance(contact)
                        dialog.setOnUpdateListener { updatedContact ->
                            viewModel.updateContact(updatedContact.id, updatedContact.name, updatedContact.phone)
                        }
                        dialog.show(parentFragmentManager, "update_dialog")
                    }
                }
                adapter.notifyItemChanged(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
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