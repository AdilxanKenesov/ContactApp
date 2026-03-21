package uz.gita.contactapp.presenter.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import okhttp3.OkHttpClient
import uz.gita.contactapp.data.repository.AuthRepositoryImpl
import uz.gita.contactapp.data.repository.ContactRepositoryImpl

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(AuthRepositoryImpl(), ContactRepositoryImpl(Gson(), OkHttpClient())) as T
    }
}