package uz.gita.contactapp.data.model.contact.request

data class UpdateContactRequest(
    val id: Long,
    val name: String,
    val phone: String
)
