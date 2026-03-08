package uz.gita.contactapp.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import uz.gita.contactapp.data.local.TokenManager
import java.io.IOException

class AuthRepositoryImpl: AuthRepository {
    private val client = OkHttpClient()

    companion object{
        private lateinit var instance: AuthRepository
        fun getInstance(): AuthRepository{
            if (!(::instance.isInitialized)){
                instance = AuthRepositoryImpl()
            }
            return instance
        }
    }


    override fun register(request: AuthRequest, onSuccess: (GenericResponse<AuthData>) -> Unit, onError: (String) -> Unit) {
        val gson = Gson()
        val json = gson.toJson(request)
        val body = json.toRequestBody(
            "application/json; charset=utf-8".toMediaType()
        )
        val httpRequest = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/register")
            .post(body)
            .build()

        client.newCall(httpRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error Register")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null){
                    onError("Server error: ${response.code}")
                    return
                }

                try {
                    val type = object : TypeToken<GenericResponse<AuthData>>(){}.type
                    val result: GenericResponse<AuthData> = gson.fromJson(responseBody, type)
                    onSuccess(result)

                    result.data?.token?.let { token->
                        TokenManager.saveToken(token)
                    }

                    TokenManager.saveUserData(name = request.name, password = request.password)

                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }


            }

        })

    }

    override fun login(request: AuthRequest, onSuccess: (GenericResponse<AuthData>) -> Unit, onError: (String) -> Unit) {
        val gson = Gson()
        val json = gson.toJson(request)
        val body = json.toRequestBody(
            "application/json; charset=utf-8".toMediaType()
        )

        val httpRequest = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/login")
            .post(body)
            .build()
        client.newCall(httpRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error login")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null){
                    onError("Server error: ${response.code}")
                    return
                }
                try {
                    val type = object : TypeToken<GenericResponse<AuthData>>(){}.type
                    val result: GenericResponse<AuthData> = gson.fromJson(responseBody, type)
                    onSuccess(result)
                    result.data?.token?.let { token ->
                        TokenManager.saveToken(token)
                    }
                    TokenManager.saveUserData(request.name, request.password)
                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }
            }

        })
    }

    override fun logout(request: AuthRequest, onSuccess: (GenericResponse<Unit>) -> Unit, onError: (String) -> Unit) {
        val gson = Gson()
        val json = gson.toJson(request)
        val body = json.toRequestBody(
            "application/json; charset=utf-8".toMediaType()
        )
        val httpRequest = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/logout")
            .post(body)
            .build()

        client.newCall(httpRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error logout")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null){
                    onError("Server error: ${response.code}")
                    return
                }

                try {
                    val type = object : TypeToken<GenericResponse<Unit>>(){}.type
                    val result: GenericResponse<Unit> = gson.fromJson(responseBody, type)

                    TokenManager.clearAll()
                    onSuccess(result)
                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }

            }

        })

    }

    override fun delete(request: AuthRequest, onSuccess: (GenericResponse<DeleteAccountData>) -> Unit, onError: (String) -> Unit) {
        val gson = Gson()
        val json = gson.toJson(request)
        val body = json.toRequestBody(
            "application/json; charset=utf-8".toMediaType()
        )

        val httpRequest = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/unregister")
            .post(body)
            .build()

        client.newCall(httpRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error delete")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null){
                    onError("Server error: ${response.code}")
                    return
                }

                try {
                    val type = object : TypeToken<GenericResponse<DeleteAccountData>>(){}.type
                    val result: GenericResponse<DeleteAccountData> = gson.fromJson(responseBody, type)

                    TokenManager.clearAll()
                    onSuccess(result)
                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }
            }

        })
    }
}