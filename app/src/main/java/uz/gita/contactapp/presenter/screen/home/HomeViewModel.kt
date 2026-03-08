package uz.gita.contactapp.presenter.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.repository.AuthRepository

class HomeViewModel(private val repo: AuthRepository): ViewModel() {
    private val _logoutResult = MutableLiveData<GenericResponse<Unit>>()
    val logoutResult: LiveData<GenericResponse<Unit>> get() = _logoutResult

    private val _deleteResult = MutableLiveData<GenericResponse<DeleteAccountData>>()
    val deleteResult: LiveData<GenericResponse<DeleteAccountData>> get() = _deleteResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun logout(){
        val name = TokenManager.getName()
        val password = TokenManager.getPassword()

        if (name == null || password == null){
            _error.postValue("User data not found")
            return
        }
        val request = AuthRequest(name,password)

        repo.logout(
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

        repo.delete(
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