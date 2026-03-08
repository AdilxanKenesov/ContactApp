package uz.gita.contactapp.presenter.screen.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        val request = AuthRequest(name, password)
        repo.register(request = request,
            onSuccess = { response ->
                _registerResult.postValue(response)
            },
            onError = { errMsg ->
                _error.postValue(errMsg)
            })

    }


}