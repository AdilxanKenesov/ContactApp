package uz.gita.contactapp.presenter.screen.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.repository.AuthRepository

class RegisterViewModel(private val repo: AuthRepository): ViewModel() {

    private val _registerResult = MutableLiveData<GenericResponse<AuthData>>()
    val registerResult: LiveData<GenericResponse<AuthData>> get() = _registerResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun register(name: String, password: String){
        viewModelScope.launch {
            val result = repo.register(AuthRequest(name,password))

            result.onSuccess{response ->
                _registerResult.value = response
            }.onFailure {
                _error.value = it.message ?: "Error"
            }
        }

    }


}