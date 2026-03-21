package uz.gita.contactapp.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.model.contact.request.ContactRequest
import uz.gita.contactapp.data.model.contact.request.UpdateContactRequest
import uz.gita.contactapp.data.model.contact.response.ContactResponse
import java.io.IOException


class ContactRepositoryImpl(private val gson: Gson, private val client: OkHttpClient): ContactRepository {
    companion object{
        private lateinit var instance: ContactRepository

        fun getInstance(): ContactRepository{
            if (!(::instance.isInitialized)){
                instance = ContactRepositoryImpl(Gson(), OkHttpClient())
            }
            return instance
        }
    }

    override fun getContacts(onSuccess: (GenericResponse<List<ContactResponse>>) -> Unit, onError: (String) -> Unit) {

        val request = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/contact")
            .addHeader("token", TokenManager.getToken() ?: "")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error getContact")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                if (body == null) {
                    onError("Empty response")
                    return
                }

                try {
                    val type = object : TypeToken<GenericResponse<List<ContactResponse>>>() {}.type
                    val result: GenericResponse<List<ContactResponse>> = gson.fromJson(body, type)
                    if (response.isSuccessful){
                        onSuccess(result)
                    }else{
                        onError(result.message)
                    }
                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }

            }

        })
    }

    override fun addContact(request: ContactRequest, onSuccess: (GenericResponse<ContactResponse>) -> Unit, onError: (String) -> Unit) {
        val json = gson.toJson(request)
        val body = json.toRequestBody(
            "application/json; charset=utf-8".toMediaType()

        )
        val token = TokenManager.getToken()
        val htthRequest = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/contact")
            .addHeader("token",token ?: "")
            .post(body = body)
            .build()

        client.newCall(htthRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error add contact")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null){
                    onError("Server error: ${response.code}")
                    return
                }
                try {
                    val type = object : TypeToken<GenericResponse<ContactResponse>>(){}.type
                    val result: GenericResponse<ContactResponse> = gson.fromJson(responseBody,type)
                    onSuccess(result)
                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }
            }
        })
    }

    override fun updateContact(request: UpdateContactRequest, onSuccess: (GenericResponse<ContactResponse>) -> Unit, onError: (String) -> Unit) {
        val json = gson.toJson(request)
        val body = json.toRequestBody(
            "application/json; charset=utf-8".toMediaType()

        )
        val htthRequest = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/contact")
            .addHeader("token", TokenManager.getToken() ?: "")
            .put(body = body)
            .build()

        client.newCall(htthRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error add contact")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null){
                    onError("Server error: ${response.code}")
                    return
                }
                try {
                    val type = object : TypeToken<GenericResponse<ContactResponse>>(){}.type
                    val result: GenericResponse<ContactResponse> = gson.fromJson(responseBody,type)
                    onSuccess(result)
                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }
            }
        }
        )
    }

    override fun deleteContact(id: Int, onSuccess: (GenericResponse<Unit>) -> Unit, onError: (String) -> Unit) {

        val htthRequest = Request.Builder()
            .url("https://pledgeable-marly-hubert.ngrok-free.dev/contact/delete?id=$id")
            .addHeader("token", TokenManager.getToken() ?: "")
            .delete()
            .build()

        client.newCall(htthRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Error delete contact")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null){
                    onError("Server error: ${response.code}")
                    return
                }
                try {
                    val type = object : TypeToken<GenericResponse<Unit>>(){}.type
                    val result: GenericResponse<Unit> = gson.fromJson(responseBody,type)
                    onSuccess(result)
                }catch (e: Exception){
                    onError("Parsing error: ${e.message}")
                }
            }
        }
        )
    }

}