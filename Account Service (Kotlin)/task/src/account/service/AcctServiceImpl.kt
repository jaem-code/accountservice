package account.service

import account.dto.PaymentResult
import account.dto.PaymentsRequest
import account.entity.Payment
import account.exception.PaymentsException
import account.repository.PaymentRepository
import account.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AcctServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
): AcctService {

    override fun addPayments(paymentsRequest: List<PaymentsRequest>): PaymentResult {
        for (paymentRequest in paymentsRequest) {
            // 유효성 검사 - DB에 있는 사용자 인지 확인
            val existingUser = userRepository.findByEmailIgnoreCase(paymentRequest.employee)
            if (existingUser == null) {
                throw PaymentsException("Post: User aren't existed")
            }
            // Must be non-negative
            if (paymentRequest.salary.toInt() < 0) {
                throw PaymentsException("Post: non-negative error")
            }
            // Validate Unique
            val existingPayment = paymentRepository.findByEmployeeAndPeriod(
                paymentRequest.employee, paymentRequest.period
            )
            if (existingPayment != null) {
                throw PaymentsException("Post: Unique error")
            }

            // Create and save payment
            val payment = Payment(
                employee = paymentRequest.employee,
                period = paymentRequest.period,
                salary = paymentRequest.salary
            )
            paymentRepository.save(payment)
        }
        return PaymentResult(status = "Added successfully!")
    }

    @Transactional
    override fun updatePayments(updatePayments: PaymentsRequest): PaymentResult {
        //유효성 검사: updatePayments의 employee가 DB 에 있는지 확인
        val existingUpdateUser = userRepository.findByEmailIgnoreCase(updatePayments.employee)

        if (existingUpdateUser == null) {
            throw PaymentsException("Put: User aren't existed")
        }

        //
        val updatePaymentsUser = paymentRepository.findByEmployeeAndPeriod(
            updatePayments.employee, updatePayments.period
        )
        if (updatePaymentsUser != null) {
            // Modify the entity's fields
            updatePaymentsUser.salary = updatePayments.salary
            // Save the updated entity
            paymentRepository.save(updatePaymentsUser)
        }
        return PaymentResult(status = "Added successfully!")
    }


}