package account.controller

import account.dto.ChangePasswordRequest
import account.dto.SignUpRequest
import account.dto.SignUpResponse
import account.exception.*
import account.service.CustomUserDetails
import account.service.PasswordService
import account.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val passwordService: PasswordService,
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
            // 비밀번호 유출 여부 검증
            if (passwordService.isPasswordBreached(signUpRequest.password)) {
                throw BadRequestException("The password is in the hacker's database!")
            }

            val user = userService.registerUser(signUpRequest)
            val response = SignUpResponse(
                id = user.id,
                name = user.name,
                lastname = user.lastname,
                email = user.email
            )
            return ResponseEntity.status(HttpStatus.OK).body(response)
        } catch (ex: UserAlreadyExistsException) {
            throw UserAlreadyExistsException("User exist!")
        }
    }

    @PostMapping("/changepass")
    fun changePassword(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody @Valid request: ChangePasswordRequest
    ): ResponseEntity<Any> {
        val username = userDetails.username
        val newPassword = request.newPassword

        // 비밀번호 유출 여부 검증
        if (passwordService.isPasswordBreached(newPassword)) {
            throw PasswordExceptions("The password is in the hacker's database!")
        }

        // 이전 비밀번호 검증
        if (passwordService.isNewPasswordValid(username, newPassword)) {
            throw BadRequestException("The passwords must be different!")
        }

        // 새로운 비밀번호의 보안 요구 사항 검증
        if (!passwordService.isPasswordValid(newPassword)) {
            throw BadRequestException("Password length must be 12 chars minimum!")
        }

        // 비밀번호 업데이트
        passwordService.updateStoredPasswordHash(username, newPassword)

        val response = mapOf(
            "email" to username.lowercase(),
            "status" to "The password has been updated successfully"
        )
        return ResponseEntity.ok(response)
    }
}