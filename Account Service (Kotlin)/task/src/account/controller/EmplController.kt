package account.controller

import account.repository.PaymentRepository
import account.service.CustomUserDetails
import account.service.EmplServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/empl")
class EmplController(
    private val emplService: EmplServiceImpl,
    private val paymentRepository: PaymentRepository
) {
    @GetMapping("/payment")
    fun getPayment(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestParam(required = false) period: String?,
    ): ResponseEntity<Any> {
        // PaymentRepository에 Data 가 있는지 확인. User는 있지만, Data가 없는 경우도 존재.
        val checkPaymentRepository = paymentRepository.findByEmployeeIgnoreCase(userDetails.getEmail())

        // Data가 있을 때, period 체크
        if (checkPaymentRepository.isNotEmpty()) {
            if (period != null ) {
                val periodNotNull = emplService.getPaymentByPeriodAndEmployee(period, userDetails.getEmail())
                return ResponseEntity.status(HttpStatus.OK).body(periodNotNull)
            } else {
                val periodNull = emplService.getPaymentByEmployee(userDetails.getEmail())
                return ResponseEntity.status(HttpStatus.OK).body(periodNull)
            }
        }

        // 오류를 해결하기 위해 추가한 부분: 모든 경우에 반환값을 제공
        return ResponseEntity.status(HttpStatus.OK).body("User or payment information not found.")
    }
}




