package uz.gita.contactapp.data.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse

interface AuthRepository {
    suspend fun register(request: AuthRequest): Flow<Result<GenericResponse<AuthData>>>
    suspend fun login(request: AuthRequest): Flow<Result<GenericResponse<AuthData>>>
    suspend fun logout(request: AuthRequest): Flow<Result<GenericResponse<Unit>>>
    suspend fun delete(request: AuthRequest): Flow<Result<GenericResponse<DeleteAccountData>>>
}