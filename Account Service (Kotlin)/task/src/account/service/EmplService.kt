package account.service

import account.dto.PaymentResponse

interface EmplService {
    fun getPaymentByPeriodAndEmployee(period: String, employee: String): PaymentResponse?

    fun getPaymentByEmployee(employee: String): List<PaymentResponse>
}