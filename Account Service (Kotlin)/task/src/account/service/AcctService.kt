package account.service

import account.dto.PaymentResponse
import account.dto.PaymentResult
import account.dto.PaymentsRequest


interface AcctService {
    fun addPayments(paymentsRequest: List<PaymentsRequest>): PaymentResult

    fun updatePayments(updatePayments: PaymentsRequest): PaymentResult
}