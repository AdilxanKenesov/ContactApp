package uz.gita.contactapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    override suspend fun register(request: AuthRequest): Result<GenericResponse<AuthData>> {
        return withContext(Dispatchers.IO){
            try {
                val response = authApi.register(request)

                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        result.data?.token?.let { token ->
                            TokenManager.saveToken(token)
                        }
                        TokenManager.saveUserData(request.name, request.password)
                        Result.success(result)
                    } else {
                        Result.failure(Exception("Server Error"))
                    }
                }else{
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            }catch (e: Exception){
                Result.failure(e)
            }
        }
    }

    override suspend fun login(request: AuthRequest): Result<GenericResponse<AuthData>> {
        return withContext(Dispatchers.IO){
            try {
                val response = authApi.login(request)
                if (response.isSuccessful){
                    val result = response.body()
                    if (result != null){
                        result.data?.token?.let { token ->
                            TokenManager.saveToken(token)
                        }
                        TokenManager.saveUserData(request.name, request.password)
                        Result.success(result)
                    }else{
                        Result.failure(Exception("Server Error"))
                    }
                }else{
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            }catch (e: Exception){
                Result.failure(e)
            }
        }
    }

    override suspend fun logout(request: AuthRequest): Result<GenericResponse<Unit>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.logout(request)
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        TokenManager.clearAll()
                        Result.success(result)
                    } else Result.failure(Exception("Body is null"))
                } else Result.failure(Exception("Error: ${response.code()}"))
            } catch (e: Exception) { Result.failure(e) }
        }
    }

    override suspend fun delete(request: AuthRequest): Result<GenericResponse<DeleteAccountData>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.deleteAccount(request)

                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        TokenManager.clearAll()
                        Result.success(result)
                    } else Result.failure(Exception("Body is null"))
                } else {
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}