package account.entity

import javax.persistence.*

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

    var period: String,

    var salary: String,
)