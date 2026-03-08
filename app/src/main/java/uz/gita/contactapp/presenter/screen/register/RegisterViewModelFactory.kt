package uz.gita.contactapp.presenter.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.contactapp.data.repository.AuthRepositoryImpl

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(AuthRepositoryImpl.getInstance()) as T
    }
}