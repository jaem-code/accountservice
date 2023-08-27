package account.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

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
    val password: String
)
