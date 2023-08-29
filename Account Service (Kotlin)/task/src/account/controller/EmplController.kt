package account.controller

import account.dto.ErrorResponse
import account.dto.LoginDTO
import account.exception.GlobalExceptionHandler
import account.service.CustomUserDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/empl")
class EmplController(
    private val globalExceptionHandler: GlobalExceptionHandler,
) {

    @GetMapping("/payment")
    fun getPaymentInfo(@AuthenticationPrincipal userDetails: UserDetails?): ResponseEntity<Any> {
        if (userDetails != null) {
            // 인증된 사용자인 경우 로그인 정보를 DTO로 변환하여 응답
            val loggedInUser = userDetails as CustomUserDetails // CustomUserDetails로 형변환
            val loginDTO = LoginDTO(
                id = loggedInUser.getId(),
                name = loggedInUser.getName(),
                lastname = loggedInUser.getLastname(),
                email = loggedInUser.getEmail()
            )
            return ResponseEntity.ok(loginDTO)
        } else {
            // 인증되지 않은 경우 UNAUTHORIZED 응답 생성
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val errorResponse = ErrorResponse(
                timestamp = formattedDateTime,
                status = HttpStatus.UNAUTHORIZED.value(),
                error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                message = "",
                path = "/api/empl/payment"
            )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        }
    }
}

