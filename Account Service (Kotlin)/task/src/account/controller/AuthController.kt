package account.controller

import account.dto.SignUpRequest
import account.dto.SignUpResponse
import account.exception.UserAlreadyExistsException
import account.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid signUpRequest: SignUpRequest): ResponseEntity<Any> {
        try {
            val user = userService.registerUser(signUpRequest)
            val response = SignUpResponse(
                id = user.id,
                name = user.name,
                lastname = user.lastname,
                email = user.email
            )
            return ResponseEntity.status(HttpStatus.OK).body(response)
        } catch (ex: UserAlreadyExistsException) {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val responseBody = mapOf(
                "timestamp" to formattedDateTime,
                "status" to HttpStatus.BAD_REQUEST.value(),
                "error" to HttpStatus.BAD_REQUEST.reasonPhrase,
                "message" to ex.message,
                "path" to "/api/auth/signup"
            )
            return ResponseEntity.badRequest().body(responseBody)
        }
    }
}