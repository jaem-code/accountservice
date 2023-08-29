package account.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class SignUpRequest(
    @field:NotEmpty
    val name: String,

    @field:NotEmpty
    val lastname: String,

    @field:NotEmpty
    @Email
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@acme\\.com$")
    val email: String,

    @field:NotEmpty
    @field:Size(min = 12, message = "The password length must be at least 12 chars!")
    val password: String
)
