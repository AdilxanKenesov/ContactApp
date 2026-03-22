package uz.gita.contactapp.data.model.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse

interface AuthApi {
    @POST("register")
    suspend fun register(@Body requset: AuthRequest): Response<GenericResponse<AuthData>>

    @POST("login")
    suspend fun login(@Body request: AuthRequest): Response<GenericResponse<AuthData>>

    @POST("logout")
    suspend fun logout(@Body request: AuthRequest): Response<GenericResponse<Unit>>

    @POST("unregister")
    suspend fun deleteAccount(@Body request: AuthRequest): Response<GenericResponse<DeleteAccountData>>

}