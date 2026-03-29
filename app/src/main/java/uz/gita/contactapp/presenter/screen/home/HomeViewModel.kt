package uz.gita.contactapp.presenter.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.contact.request.ContactRequest
import uz.gita.contactapp.data.model.contact.request.UpdateContactRequest
import uz.gita.contactapp.data.model.contact.response.ContactResponse
import uz.gita.contactapp.data.repository.AuthRepository
import uz.gita.contactapp.data.repository.ContactRepository
import uz.gita.contactapp.util.UiState

class HomeViewModel(
    private val authRepo: AuthRepository,
    private val contactRepo: ContactRepository
) : ViewModel() {

    private val _logoutResult = MutableStateFlow<UiState<Unit>?>(null)
    val logoutResult: StateFlow<UiState<Unit>?> = _logoutResult.asStateFlow()

    private val _deleteAccountResult = MutableStateFlow<UiState<DeleteAccountData>?>(null)
    val deleteAccountResult: StateFlow<UiState<DeleteAccountData>?> = _deleteAccountResult.asStateFlow()

    private val _contactsResult = MutableStateFlow<UiState<List<ContactResponse>>?>(null)
    val contactsResult: StateFlow<UiState<List<ContactResponse>>?> = _contactsResult.asStateFlow()

    private val _addContactResult = MutableStateFlow<UiState<ContactResponse>?>(null)
    val addContactResult: StateFlow<UiState<ContactResponse>?> = _addContactResult.asStateFlow()

    private val _deleteContactResult = MutableStateFlow<UiState<Unit>?>(null)
    val deleteContactResult: StateFlow<UiState<Unit>?> = _deleteContactResult.asStateFlow()

    private val _updateContactResult = MutableStateFlow<UiState<ContactResponse>?>(null)
    val updateContactResult: StateFlow<UiState<ContactResponse>?> = _updateContactResult.asStateFlow()

    init {
        loadContacts()
    }

    fun loadContacts() {
        viewModelScope.launch {
            contactRepo.getContacts()
                .onStart { _contactsResult.value = UiState.Loading }
                .catch { _contactsResult.value = UiState.Error(it.message ?: "Error") }
                .collect { result ->
                    result.onSuccess { _contactsResult.value = UiState.Success(it.data!!) }
                        .onFailure { _contactsResult.value = UiState.Error(it.message ?: "Error") }
                }
        }
    }

    fun addContact(name: String, phone: String) {
        viewModelScope.launch {
            contactRepo.addContact(ContactRequest(name, phone))
                .onStart { _addContactResult.value = UiState.Loading }
                .collect { result ->
                    result.onSuccess {
                        _addContactResult.value = UiState.Success(it.data!!)
                        loadContacts()
                    }.onFailure { _addContactResult.value = UiState.Error(it.message ?: "Error") }
                }
        }
    }

    fun updateContact(id: Long, name: String, phone: String) {
        viewModelScope.launch {
            contactRepo.updateContact(UpdateContactRequest(id, name, phone))
                .onStart { _updateContactResult.value = UiState.Loading }
                .collect { result ->
                    result.onSuccess {
                        _updateContactResult.value = UiState.Success(it.data!!)
                        loadContacts()
                    }.onFailure { _updateContactResult.value = UiState.Error(it.message ?: "Error") }
                }
        }
    }

    fun deleteContact(id: Int) {
        viewModelScope.launch {
            contactRepo.deleteContact(id)
                .onStart { _deleteContactResult.value = UiState.Loading }
                .collect { result ->
                    result.onSuccess {
                        _deleteContactResult.value = UiState.Success(Unit)
                        loadContacts()
                    }.onFailure { _deleteContactResult.value = UiState.Error(it.message ?: "Error") }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val request = AuthRequest(TokenManager.getName() ?: "", TokenManager.getPassword() ?: "")
            authRepo.logout(request)
                .onStart { _logoutResult.value = UiState.Loading }
                .collect { result ->
                    result.onSuccess { _logoutResult.value = UiState.Success(Unit) }
                        .onFailure { _logoutResult.value = UiState.Error(it.message ?: "Error") }
                }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val request = AuthRequest(TokenManager.getName() ?: "", TokenManager.getPassword() ?: "")
            authRepo.delete(request)
                .onStart { _deleteAccountResult.value = UiState.Loading }
                .collect { result ->
                    result.onSuccess { _deleteAccountResult.value = UiState.Success(it.data!!) }
                        .onFailure { _deleteAccountResult.value = UiState.Error(it.message ?: "Error") }
                }
        }
    }

    fun resetContactStates() {
        _addContactResult.value = null
        _updateContactResult.value = null
        _deleteContactResult.value = null
    }
}