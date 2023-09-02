package account.service

import account.dto.PaymentResponse
import account.exception.PaymentsException
import account.repository.PaymentRepository
import account.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class EmplServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository

):EmplService {
    private fun formatSalary(salary: Long): String {
        val dollars = salary / 100
        val cents = salary % 100
        return "$dollars dollar(s) $cents cent(s)"
    }

    private fun formatPeriod(period: String): String {
        val monthNames = listOf(
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        )
        val (month, year) = period.split("-")
        val formattedMonth = monthNames[month.toInt() - 1]

        return "$formattedMonth-$year"
    }

    private fun convertToMonthName(period: String): String {
        val monthNames = listOf(
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        )

        val (month, year) = period.split("-")
        val formattedMonth = monthNames[month.toInt() - 1]
        return "$formattedMonth-$year"
    }

    // GET - getPayment (Parameter 있을 때)
    override fun getPaymentByPeriodAndEmployee(period: String, employee: String): PaymentResponse? {
        val payment = paymentRepository.findByEmployeeAndPeriod(employee, period)
        val user = userRepository.findByEmailIgnoreCase(employee)

        // 해당하는 데이터 없을 때, exception
        if (payment == null) {
            throw PaymentsException("Error!")
        }
        // 해당하는 데이터 있을 때,
        val getPaymentResponse = PaymentResponse(
            name = user?.name,
            lastname = user?.lastname,
            period = formatPeriod(payment.period),
            salary = formatSalary(payment.salary.toLong())
        )
        return getPaymentResponse
    }

    // GET - getPayment (Parameter 없을 때)
    override fun getPaymentByEmployee(employee: String): List<PaymentResponse> {
        val payments = paymentRepository.findByEmployee(employee)
        val user = userRepository.findByEmailIgnoreCase(employee)

        if(payments.isEmpty()) {
            throw PaymentsException("No Data")
        }

        // payments 데이터가 있을 때,
        val paymentResponses = mutableListOf<PaymentResponse>()
        for (payment in payments) {
            val paymentResponse = PaymentResponse(
                name = user?.name,
                lastname = user?.lastname,
                period = payment.period,
                salary = formatSalary(payment.salary.toLong())
            )
            paymentResponses.add(paymentResponse)
        }

        val sortedPaymentResponses = paymentResponses.sortedByDescending { it.period }


        val transformedList = sortedPaymentResponses.map { payment ->
            payment.copy(period = convertToMonthName(payment.period))
        }

        return transformedList
    }
}