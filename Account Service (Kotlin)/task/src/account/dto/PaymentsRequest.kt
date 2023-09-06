package account.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class PaymentsRequest(
    @field:NotEmpty
    @Email
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@acme\\.com$")
    var employee: String,

    @field:NotNull
    @field:Pattern(regexp = "^(0[1-9]|1[0-2])-\\d{4}\$")
    var period: String,

    @field:NotNull
    var salary: Long
)