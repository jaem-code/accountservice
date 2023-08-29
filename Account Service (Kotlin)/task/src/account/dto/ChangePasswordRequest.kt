package account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class ChangePasswordRequest(
    @field:NotEmpty
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @JsonProperty(value = "new_password")
    val newPassword: String
)

