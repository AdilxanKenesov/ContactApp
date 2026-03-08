package uz.gita.contactapp.presenter.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.contactapp.data.repository.AuthRepositoryImpl

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(AuthRepositoryImpl()) as T
    }
}