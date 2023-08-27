package account.controller

import account.dto.ErrorResponseDTO
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
    /**
     * 회원 가입을 처리하는 엔드포인트입니다.
     *
     * @param signUpRequest 가입 요청 객체. @RequestBody 어노테이션을 통해 요청 바디의 내용과 매핑됩니다.
     * @return 가입이 성공하면 200 OK 상태 코드와 가입된 사용자 정보를 포함한 응답을 반환합니다.
     *         이미 가입된 이메일인 경우 400 Bad Request 상태 코드와 에러 메시지를 반환합니다.
     */
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

            val errorResponse = ErrorResponseDTO(
                timestamp = formattedDateTime,
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.reasonPhrase,
                message = ex.message ?: "",
                path = "/api/auth/signup"
            )
            return ResponseEntity.badRequest().body(errorResponse)
        }
    }
}
