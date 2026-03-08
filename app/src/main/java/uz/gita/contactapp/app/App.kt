package uz.gita.contactapp.app

import android.app.Application
import uz.gita.contactapp.data.local.TokenManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}