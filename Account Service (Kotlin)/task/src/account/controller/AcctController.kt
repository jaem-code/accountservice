package account.controller

import account.dto.PaymentsRequest
import account.exception.PaymentsException
import account.service.AcctServiceImpl
import account.service.CustomUserDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.Period


@RestController
@RequestMapping("/api/acct")
class AcctController(
    private val acctService: AcctServiceImpl,

) {
    @PostMapping("/payments")
    fun addPayments(@RequestBody paymentsRequest: List<PaymentsRequest>):ResponseEntity<Any> {
            val addPaymentsRequest = acctService.addPayments(paymentsRequest)
            return ResponseEntity.status(HttpStatus.OK).body(addPaymentsRequest)
    }

    @Transactional
    @PutMapping("/payments")
    fun updatePayments(@RequestBody updatePayments: PaymentsRequest): ResponseEntity<Any>{
        val updatePaymentsRequest = acctService.updatePayments(updatePayments)
        return ResponseEntity.status(HttpStatus.OK).body(updatePaymentsRequest)
    }
}