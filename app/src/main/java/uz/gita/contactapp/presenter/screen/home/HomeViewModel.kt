package uz.gita.contactapp.presenter.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.model.contact.request.ContactRequest
import uz.gita.contactapp.data.model.contact.request.UpdateContactRequest
import uz.gita.contactapp.data.model.contact.response.ContactResponse
import uz.gita.contactapp.data.repository.AuthRepository
import uz.gita.contactapp.data.repository.ContactRepository

class HomeViewModel(private val Authrepo: AuthRepository, private val contactRepo: ContactRepository): ViewModel() {
    private val _logoutResult = MutableLiveData<GenericResponse<Unit>>()
    val logoutResult: LiveData<GenericResponse<Unit>> get() = _logoutResult
    private val _addContact = MutableLiveData<GenericResponse<ContactResponse>>()
    val addContact: LiveData<GenericResponse<ContactResponse>> get() = _addContact

    private val _deleteResult = MutableLiveData<GenericResponse<DeleteAccountData>>()
    val deleteResult: LiveData<GenericResponse<DeleteAccountData>> get() = _deleteResult
    private val _deleteContactResult = MutableLiveData<GenericResponse<Unit>>()
    val deleteContactResult: LiveData<GenericResponse<Unit>> get() = _deleteContactResult

    private val _updateContactResult = MutableLiveData<GenericResponse<ContactResponse>>()
    val updateContactResult: LiveData<GenericResponse<ContactResponse>> get() = _updateContactResult

    private val _getContacts = MutableLiveData<GenericResponse<List<ContactResponse>>>()
    val getContacts: LiveData<GenericResponse<List<ContactResponse>>> get() = _getContacts
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error



    init {
        loadContact()
    }


    fun loadContact(){
        contactRepo.getContacts(
            onSuccess = {result->
                _getContacts.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
    fun deleteContact(id: Int){
        contactRepo.deleteContact(
            id = id,
            onSuccess = {
                _deleteContactResult.postValue(it)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
    fun updateContact(id: Long, name: String, phone: String){
        contactRepo.updateContact(
            request = UpdateContactRequest(id,name,phone),
            onSuccess = {
                _updateContactResult.postValue(it)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
    fun addContact(name: String, phone: String){
        val request = ContactRequest(name,phone)

        contactRepo.addContact(
            request,
            onSuccess = {result->
                _addContact.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
    fun logout(){
        val name = TokenManager.getName()
        val password = TokenManager.getPassword()

        if (name == null || password == null){
            _error.postValue("User data not found")
            return
        }
        val request = AuthRequest(name,password)

        TokenManager.clearAll()
        Authrepo.logout(
            request = request,
            onSuccess = {result ->
                _logoutResult.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
    fun deleteAccount(){
        val name = TokenManager.getName()
        val password = TokenManager.getPassword()

        if (name == null || password == null){
            _error.postValue("User data not found")
            return
        }

        val request = AuthRequest(name,password)

        Authrepo.delete(
            request = request,
            onSuccess = {result->
                _deleteResult.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

}