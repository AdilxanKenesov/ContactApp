package uz.gita.contactapp.data.repository

import uz.gita.contactapp.data.model.auth.request.AuthRequest
import uz.gita.contactapp.data.model.auth.response.AuthData
import uz.gita.contactapp.data.model.auth.response.DeleteAccountData
import uz.gita.contactapp.data.model.auth.response.GenericResponse

interface AuthRepository {
    fun register(request: AuthRequest, onSuccess: (GenericResponse<AuthData>) -> Unit, onError: (String) -> Unit)
    fun login(request: AuthRequest, onSuccess: (GenericResponse<AuthData>) -> Unit, onError: (String) -> Unit)
    fun logout(request: AuthRequest, onSuccess: (GenericResponse<Unit>) -> Unit, onError: (String) -> Unit)
    fun delete(request: AuthRequest, onSuccess: (GenericResponse<DeleteAccountData>) -> Unit, onError: (String) -> Unit)
}