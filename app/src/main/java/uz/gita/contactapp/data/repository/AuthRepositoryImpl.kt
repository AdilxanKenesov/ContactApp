package uz.gita.contactapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.data.model.network.ApiClient
import uz.gita.contactapp.data.model.network.api.AuthApi

class AuthRepositoryImpl(private val authApi: AuthApi): AuthRepository {
    companion object{
        private lateinit var instance: AuthRepository
        fun getInstance(): AuthRepository{
            if (!(::instance.isInitialized)){
                instance = AuthRepositoryImpl(ApiClient.authApi)
            }
            return instance
        }
    }

    override suspend fun register(request: AuthRequest): Flow<Result<GenericResponse<AuthData>>> = flow{
            try {
                val response = authApi.register(request)

                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        result.data?.token?.let { token ->
                            TokenManager.saveToken(token)
                        }
                        TokenManager.saveUserData(request.name, request.password)
                        emit(Result.success(result))
                    } else {
                        emit(Result.failure(Exception("Server Error")))
                    }
                }else{
                    emit(Result.failure(Exception("Error: ${response.code()}")))
                }
            }catch (e: Exception){
                emit(Result.failure(e))
            }

    }

    override suspend fun login(request: AuthRequest): Flow<Result<GenericResponse<AuthData>>> = flow {
        try {
                val response = authApi.login(request)
                if (response.isSuccessful){
                    val result = response.body()
                    if (result != null){
                        result.data?.token?.let { token ->
                            TokenManager.saveToken(token)
                        }
                        TokenManager.saveUserData(request.name, request.password)
                        emit(Result.success(result))
                    }else{
                        emit(Result.failure(Exception("Server Error")))
                    }
                }else{
                    emit(Result.failure(Exception("Error: ${response.code()}")))
                }
            }catch (e: Exception){
                emit(Result.failure(e))
            }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout(request: AuthRequest): Flow<Result<GenericResponse<Unit>>> = flow {
            try {
                val response = authApi.logout(request)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        TokenManager.clearAll()
                        emit(Result.success(result))
                    } else emit(Result.failure(Exception("Body is null")))
                } else emit(Result.failure(Exception("Error: ${response.code()}")))
            } catch (e: Exception) { emit(Result.failure(e)) }
    }.flowOn(Dispatchers.IO)

    override suspend fun delete(request: AuthRequest): Flow<Result<GenericResponse<DeleteAccountData>>> = flow {
        try {
            val response = authApi.deleteAccount(request)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        TokenManager.clearAll()
                        emit(Result.success(result))
                    } else emit(Result.failure(Exception("Body is null")))
                } else {
                    emit(Result.failure(Exception("Error: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.flowOn(Dispatchers.IO)

}