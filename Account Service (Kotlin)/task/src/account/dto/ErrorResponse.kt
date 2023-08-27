package account.dto

data class ErrorResponseDTO(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)