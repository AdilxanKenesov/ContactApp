package uz.gita.contactapp.presenter.screen.home

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import uz.gita.contactapp.R
import uz.gita.contactapp.databinding.HomeScreenBinding
import uz.gita.contactapp.presenter.adapter.ContactAdapter
import uz.gita.contactapp.presenter.dialog.*
import uz.gita.contactapp.util.NotificationType
import uz.gita.contactapp.util.UiState
import uz.gita.contactapp.util.showNotification

class HomeScreen : Fragment(R.layout.home_screen) {
    private val binding by viewBinding(HomeScreenBinding::bind)
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory() }
    private val adapter by lazy { ContactAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        binding.btnMenu.setOnClickListener { showMenu() }
        binding.btnAddContact.setOnClickListener { openAddContactDialog() }

        observeViewModel()
        setupSwipeGestures()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.contactsResult.collect { state ->
                        when (state) {
                            is UiState.Loading -> showLoading(true)
                            is UiState.Success -> {
                                showLoading(false)
                                val list = state.data
                                binding.recyclerView.isVisible = list.isNotEmpty()
                                binding.lottieView.isVisible = list.isEmpty()
                                adapter.submitList(list)
                            }
                            is UiState.Error -> {
                                showLoading(false)
                                requireActivity().showNotification(state.message, NotificationType.ERROR)
                            }
                            null -> showLoading(false)
                        }
                    }
                }

                launch {
                    viewModel.logoutResult.collect { state ->
                        if (state is UiState.Success) navigateToLogin()
                        else if (state is UiState.Error) requireActivity().showNotification(state.message, NotificationType.ERROR)
                    }
                }

                launch {
                    viewModel.deleteAccountResult.collect { state ->
                        if (state is UiState.Success) navigateToLogin()
                        else if (state is UiState.Error) requireActivity().showNotification(state.message, NotificationType.ERROR)
                    }
                }

                launch {
                    viewModel.addContactResult.collect { state ->
                        if (state is UiState.Success) {
                            requireActivity().showNotification("Kontakt qo'shildi", NotificationType.SUCCESS)
                            viewModel.resetContactStates()
                        }
                    }
                }

                launch {
                    viewModel.deleteContactResult.collect { state ->
                        if (state is UiState.Success) {
                            requireActivity().showNotification("Kontakt o'chirildi", NotificationType.SUCCESS)
                            viewModel.resetContactStates()
                        }
                    }
                }
                launch {
                    viewModel.updateContactResult.collect { state ->
                        if (state is UiState.Success){
                            requireActivity().showNotification("Kontakt update", NotificationType.SUCCESS)
                            viewModel.resetContactStates()
                        }
                    }
                }
            }
        }
    }

    private fun showMenu() {
        val wrapper = ContextThemeWrapper(requireContext(), R.style.Theme_LightPopup)
        val popupMenu = PopupMenu(wrapper, binding.btnMenu)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_my_info -> {
                    DialogInfoUser().show(parentFragmentManager, "info_dialog")
                    true
                }
                R.id.menu_logout -> {
                    showConfirmDialog("Chiqish", "Rostdan ham chiqmoqchimisiz?") { viewModel.logout() }
                    true
                }
                R.id.menu_delete_account -> {
                    showConfirmDialog("O'chirish", "Hisobingiz butunlay o'chib ketadi!") { viewModel.deleteAccount() }
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun openAddContactDialog() {
        val dialog = DialogAddContact()
        dialog.setListener { viewModel.addContact(it.name, it.phone) }
        dialog.show(parentFragmentManager, "add_contact_dialog")
    }

    private fun showConfirmDialog(title: String, message: String, onOk: () -> Unit) {
        val dialog = DialogMessageContact.newInstance(title, message)
        dialog.setOnOkListener { onOk() }
        dialog.show(parentFragmentManager, "confirm_dialog")
    }

    private fun setupSwipeGestures() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val contact = adapter.currentList[position]

                if (direction == ItemTouchHelper.RIGHT) {
                    val dialog = DialogDeleteContact.newInstance(contact.name)
                    dialog.setOnDeleteListener { viewModel.deleteContact(contact.id.toInt()) }
                    dialog.show(parentFragmentManager, "delete_dialog")
                } else {
                    val dialog = DialogUpdateContact.newInstance(contact)
                    dialog.setOnUpdateListener { viewModel.updateContact(it.id, it.name, it.phone) }
                    dialog.show(parentFragmentManager, "update_dialog")
                }
                adapter.notifyItemChanged(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnAddContact.isEnabled = !isLoading
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_homeScreen_to_loginScreen, null, navOptions {
            popUpTo(R.id.homeScreen) { inclusive = true }
        })
    }
}