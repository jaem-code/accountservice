package account.controller

import account.dto.LoginDTO
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
class EmplController {

    @GetMapping("/payment")
    fun getPaymentInfo(@AuthenticationPrincipal userDetails: UserDetails?
    ): ResponseEntity<Any> {
        if (userDetails != null && userDetails is CustomUserDetails) {
            val loggedInUser = userDetails // CustomUserDetails로 형변환
            val loginDTO = LoginDTO(
                id = loggedInUser.getId(),
                name = loggedInUser.getName(),
                lastname = loggedInUser.getLastname(),
                email = loggedInUser.getEmail()
            )
            return ResponseEntity.ok(loginDTO)
        } else {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val responseBody = mapOf(
                "timestamp" to formattedDateTime,
                "status" to HttpStatus.UNAUTHORIZED.value(),
                "error" to HttpStatus.UNAUTHORIZED.reasonPhrase,
                "message" to "",
                "path" to "/api/empl/payment"
            )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody)
        }
    }
}