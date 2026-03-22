package uz.gita.contactapp.data.repository

import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse

interface AuthRepository {
    suspend fun register(request: AuthRequest): Result<GenericResponse<AuthData>>
    suspend fun login(request: AuthRequest): Result<GenericResponse<AuthData>>
    suspend fun logout(request: AuthRequest): Result<GenericResponse<Unit>>
    suspend fun delete(request: AuthRequest): Result<GenericResponse<DeleteAccountData>>
}