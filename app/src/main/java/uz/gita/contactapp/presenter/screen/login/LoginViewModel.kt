package uz.gita.contactapp.presenter.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.repository.AuthRepository

class LoginViewModel(private val repo: AuthRepository): ViewModel() {
    private val _loginResult = MutableLiveData<GenericResponse<AuthData>>()
    val loginResult: LiveData<GenericResponse<AuthData>> get() = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun login(name: String, password: String){
        repo.login(
            AuthRequest(name = name, password = password),
            onSuccess = {
                _loginResult.postValue(it)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}