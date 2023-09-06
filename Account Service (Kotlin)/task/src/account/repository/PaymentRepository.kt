package account.repository

import account.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PaymentRepository: JpaRepository<Payment, Long> {
    // Payment? '?' -> Result must not be null issue
    fun findByEmployeeIgnoreCaseAndPeriod(employee: String, period: String): Payment?

    fun findByEmployeeIgnoreCase(employee: String):List<Payment>

//  fun findByEmployeeOrderByPeriodDesc(employee: String): List<Payment>
}