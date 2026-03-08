package uz.gita.contactapp.data.model.auth.response

data class GenericResponse<T>(
    val message: String,
    val data: T?
)
