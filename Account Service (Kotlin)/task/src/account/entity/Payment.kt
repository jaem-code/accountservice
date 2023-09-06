package account.entity

import javax.persistence.*
import javax.validation.constraints.Pattern

@Entity
@Table(
    name = "payment",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["employee", "period"])
    ]
)
@SequenceGenerator(name = "payment_sequence", sequenceName = "payment_sequence", allocationSize = 1)
data class Payment (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sequence")
    val id: Long? = null,

    var employee: String,

    @field:Pattern(regexp = "^(0[1-9]|1[0-2])-\\d{4}\$")
    var period: String,

    var salary: Long,
)