package uz.gita.contactapp.data.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.model.contact.request.ContactRequest
import uz.gita.contactapp.data.model.contact.request.UpdateContactRequest
import uz.gita.contactapp.data.model.contact.response.ContactResponse

interface ContactRepository {
   suspend fun getContacts(): Flow<Result<GenericResponse<List<ContactResponse>>>>
   suspend fun addContact(request: ContactRequest): Flow<Result<GenericResponse<ContactResponse>>>
    suspend fun updateContact(request: UpdateContactRequest): Flow<Result<GenericResponse<ContactResponse>>>
    suspend fun deleteContact(id: Int): Flow<Result<GenericResponse<Unit>>>

}