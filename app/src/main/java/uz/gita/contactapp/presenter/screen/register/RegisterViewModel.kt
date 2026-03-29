package uz.gita.contactapp.presenter.screen.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.repository.AuthRepository
import uz.gita.contactapp.util.UiState

class RegisterViewModel(private val repo: AuthRepository): ViewModel() {

    private val _registerResult = MutableStateFlow<UiState<AuthData>?>(null)
    val registerResult: StateFlow<UiState<AuthData>?> = _registerResult.asStateFlow()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun register(name: String, password: String){
        viewModelScope.launch {
           repo.register(AuthRequest(name,password))
               .onStart {
                   _registerResult.value = UiState.Loading
               }
               .catch {
                   _registerResult.value = UiState.Error(it.message ?: "Error")
               }
               .collect { result ->
                   result.onSuccess { response ->
                       if (response.data != null){
                           _registerResult.value = UiState.Success(response.data)
                       }else{
                           _registerResult.value = UiState.Error(response.message)
                       }
                   }.onFailure {
                       _registerResult.value = UiState.Error(it.message ?: "Error")
                   }
               }


        }

    }


}