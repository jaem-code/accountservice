package account.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "user")
@SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    val id: Long? = null,
    val name: String,
    val lastname: String,

    @Column(columnDefinition = "VARCHAR_IGNORECASE")
    val email: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @field:Size(min = 12)
    val password: String
)
