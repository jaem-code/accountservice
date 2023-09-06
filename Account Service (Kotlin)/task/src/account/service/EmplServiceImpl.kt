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
): EmplService {

    // 급여를 달러와 센트 단위로 변환하여 문자열로 포맷팅하는 함수
    private fun formatSalary(salary: Long): String {
        val dollars = salary / 100
        val cents = salary % 100
        return "$dollars dollar(s) $cents cent(s)"
    }

    // 주어진 기간을 월과 년도로 분리하여 월을 월 이름으로 변환하고 포맷팅하는 함수
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

    // 주어진 기간을 월 이름과 년도로 변환하는 함수
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

    // period가 있을 때, 주어진 기간과 직원의 이메일로 급여 정보를 조회하는 함수
    override fun getPaymentByPeriodAndEmployee(period: String, employee: String): PaymentResponse? {
        val payment = paymentRepository.findByEmployeeIgnoreCaseAndPeriod(employee, period)
        val user = userRepository.findByEmailIgnoreCase(employee)

        // period 가 mm-yyyy 형태에 벗어나는지 확인. e.g. 13-2021
        if (!Regex("^(0[1-9]|1[0-2])-\\d{4}\$").matches(period)){
            throw PaymentsException("Get: invalid period pattern")
        }

        // 해당하는 데이터 없을 때, 예외 처리
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

    // 주어진 직원의 이메일로 급여 정보를 조회하는 함수
    override fun getPaymentByEmployee(employee: String): List<PaymentResponse> {
        val payments = paymentRepository.findByEmployeeIgnoreCase(employee)
        val user = userRepository.findByEmailIgnoreCase(employee)

        // User는 존재하지만, payments 데이터가 없을 때 체크.
        if (payments.isEmpty()) {
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

        // 기간을 내림 차순으로 정렬
        val sortedPaymentResponses = paymentResponses.sortedByDescending { it.period }

        // 기간을 월 이름 형식으로 변환하여 리스트 반환
        val transformedList = sortedPaymentResponses.map { payment ->
            payment.copy(period = convertToMonthName(payment.period))
        }

        return transformedList
    }
}
