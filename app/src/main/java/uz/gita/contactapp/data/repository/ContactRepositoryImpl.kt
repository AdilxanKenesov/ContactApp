package uz.gita.contactapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.model.contact.request.ContactRequest
import uz.gita.contactapp.data.model.contact.request.UpdateContactRequest
import uz.gita.contactapp.data.model.contact.response.ContactResponse
import uz.gita.contactapp.data.model.network.ApiClient
import uz.gita.contactapp.data.model.network.api.ContactApi

class ContactRepositoryImpl(private val contactApi: ContactApi) : ContactRepository {

    companion object {
        private lateinit var instance: ContactRepository
        fun getInstance(): ContactRepository {
            if (!(::instance.isInitialized)) {
                instance = ContactRepositoryImpl(ApiClient.contactApi)
            }
            return instance
        }
    }

    private fun getToken() = TokenManager.getToken() ?: ""

    override suspend fun getContacts(): Flow<Result<GenericResponse<List<ContactResponse>>>> = flow {
        try {
            val response = contactApi.getContact(getToken())
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty body")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addContact(request: ContactRequest): Flow<Result<GenericResponse<ContactResponse>>> = flow {
        try {
            val response = contactApi.addContact(getToken(), request)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty body")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateContact(request: UpdateContactRequest): Flow<Result<GenericResponse<ContactResponse>>> = flow {
        try {
            val response = contactApi.updateContact(getToken(), request)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty body")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteContact(id: Int): Flow<Result<GenericResponse<Unit>>> = flow {
        try {
            val response = contactApi.deleteContact(getToken(), id)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty body")))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}