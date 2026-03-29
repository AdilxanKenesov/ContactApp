package uz.gita.contactapp.presenter.screen.login

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

class LoginViewModel(private val repo: AuthRepository): ViewModel() {
    private val _loginResult = MutableStateFlow<UiState<AuthData>?>(null)
    val loginResult: StateFlow<UiState<AuthData>?> = _loginResult.asStateFlow()


    fun login(name: String, password: String){
       viewModelScope.launch {
           repo.login(AuthRequest(name,password))
               .onStart {
                   _loginResult.value = UiState.Loading
               }
               .catch {
                   _loginResult.value = UiState.Error(it.message ?: "Error")
               }
               .collect { result ->
                   result.onSuccess {  response ->
                       if (response.data != null){
                           _loginResult.value = UiState.Success(response.data)
                       }else{
                           _loginResult.value = UiState.Error(response.message)
                       }
                   }.onFailure {
                       _loginResult.value = UiState.Error(it.message ?: "Error")
                   }
               }

       }
    }
}