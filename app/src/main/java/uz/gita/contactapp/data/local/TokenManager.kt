package uz.gita.contactapp.data.local

import android.content.Context
import android.content.SharedPreferences


object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_NAME = "name"
    private const val KEY_PASSWORD = "password"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUserData(name: String, password: String){
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_PASSWORD, password)
            .apply()
    }
    fun getName(): String? {
        return prefs.getString(KEY_NAME, null)
    }
    fun getPassword(): String? {
        return prefs.getString(KEY_PASSWORD, null)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}