package uz.gita.contactapp.data.model.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import uz.gita.contactapp.data.model.auth.response.GenericResponse
import uz.gita.contactapp.data.model.contact.request.ContactRequest
import uz.gita.contactapp.data.model.contact.request.UpdateContactRequest
import uz.gita.contactapp.data.model.contact.response.ContactResponse

interface ContactApi {
    @GET("contact")
    suspend fun getContact(@Header("token") token: String): Response<GenericResponse<List<ContactResponse>>>

    @POST("contact")
    suspend fun addContact(@Header("token") token: String, @Body request: ContactRequest): Response<GenericResponse<ContactResponse>>

    @PUT("contact")
    suspend fun updateContact(@Header("token") token: String, @Body request: UpdateContactRequest): Response<GenericResponse<ContactResponse>>

    @DELETE("contact/delete")
    suspend fun deleteContact(@Header("token") token: String, @Query("id") id: Int): Response<GenericResponse<Unit>>
}