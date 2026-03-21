package uz.gita.contactapp.data.repository

import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.model.contact.request.ContactRequest
import uz.gita.contactapp.data.model.contact.request.UpdateContactRequest
import uz.gita.contactapp.data.model.contact.response.ContactResponse

interface ContactRepository {
    fun getContacts(onSuccess: (GenericResponse<List<ContactResponse>>) -> Unit, onError: (String) -> Unit)
    fun addContact(request: ContactRequest, onSuccess: (GenericResponse<ContactResponse>) -> Unit, onError: (String) -> Unit)
    fun updateContact(request: UpdateContactRequest, onSuccess: (GenericResponse<ContactResponse>) -> Unit, onError: (String) -> Unit)
    fun deleteContact(id: Int, onSuccess: (GenericResponse<Unit>) -> Unit, onError: (String) -> Unit)

}