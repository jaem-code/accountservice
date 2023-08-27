package account.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "user")
@SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    val id: Long? = null,
    val name: String,
    val lastname: String,

    val email: String,

    @JsonIgnore
    val password: String
)
