package account.repository

import account.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PaymentRepository: JpaRepository<Payment, Long> {
    fun findByEmployeeAndPeriod(employee: String, period: String): Payment?

    fun findByEmployee(employee: String):List<Payment>
}