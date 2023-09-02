package account.controller

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
    ) {
    @GetMapping("/payment")
    fun getPayment(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestParam(required = false) period: String?,
    ): ResponseEntity<Any> {
        return if (period != null) {
            val paymentNotNull = emplService.getPaymentByPeriodAndEmployee(period, userDetails.getEmail())
            ResponseEntity.status(HttpStatus.OK).body(paymentNotNull)
        } else {
            val paymentNull = emplService.getPaymentByEmployee(userDetails.getEmail())
            ResponseEntity.status(HttpStatus.OK).body(paymentNull)
        }
    }
}



