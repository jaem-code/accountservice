package account.controller

import account.exception.PaymentsException
import account.service.CustomUserDetails
import account.service.EmplServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empl")
class EmplController(
    private val emplService: EmplServiceImpl,
) {
    @GetMapping("/payment")
    fun getPayment(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(required = false) period: String?,
    ): ResponseEntity<Any> {
        try {
            return if (period != null) {
                // 만약 주어진 기간(period)이 있다면 해당 기간과 사용자의 이메일로 급여 정보 조회
                val paymentNotNull = emplService.getPaymentByPeriodAndEmployee(period, userDetails.username)
                ResponseEntity.status(HttpStatus.OK).body(paymentNotNull)
            } else {
                // 주어진 기간이 없다면 사용자의 이메일로 전체 급여 정보 조회
                val paymentNull = emplService.getPaymentByEmployee(userDetails.username)
                ResponseEntity.status(HttpStatus.OK).body(paymentNull)
            }
        } catch (ex: PaymentsException) {
            // 예외가 발생한 경우, 적절한 에러 메시지와 상태 코드를 반환하거나 로깅하고 예외를 처리할 수 있음
            throw PaymentsException("Why?")
        }
    }
}




