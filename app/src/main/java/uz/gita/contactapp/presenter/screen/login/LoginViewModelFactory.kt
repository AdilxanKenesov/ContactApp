package uz.gita.contactapp.presenter.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.contactapp.data.model.network.ApiClient
import uz.gita.contactapp.data.repository.AuthRepositoryImpl

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(AuthRepositoryImpl.getInstance()) as T
    }
}