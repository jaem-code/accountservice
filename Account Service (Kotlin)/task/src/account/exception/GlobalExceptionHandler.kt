package account.exception

import account.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 사용자 중복 예외
class UserAlreadyExistsException(val error: String) : RuntimeException(error)

// 사용자 없음 예외
class UserNotFoundException(val error: String) : RuntimeException(error)

// 잘못된 요청 예외
class BadRequestException(val error: String) : RuntimeException(error)

// 비밀번호 관련 예외
class PasswordExceptions(val error: String) : RuntimeException(error)

// 유효성 검증 에러 코드 정의
enum class ValidationErrorCode(val message: String) {
    NOT_EMPTY("Empty field!"),
    NOT_BLANK("Blank field!"),
    PATTERN("Wrong format"),
    SIZE("Password length must be 12 chars minimum!"),
    NULL("Null"),
    DEFAULT("Default message")
}

// REST API 예외 처리를 위한 컨트롤러 어드바이스 클래스
@RestControllerAdvice
class GlobalExceptionHandler(
    private val formatter: DateTimeFormatter
) {
    // 사용자 중복 예외 처리
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(
        e: UserAlreadyExistsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        // 예외 정보를 ErrorResponse로 포장하여 응답 생성
        val body = ErrorResponse(
            LocalDateTime.now().format(formatter),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            e.error,
            request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    // 사용자 없음 예외 처리
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        // 예외 정보를 ErrorResponse로 포장하여 응답 생성
        val body = ErrorResponse(
            LocalDateTime.now().format(formatter),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            e.error,
            request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    // 잘못된 요청 예외 처리
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException, request: WebRequest): ResponseEntity<ErrorResponse> {
        // 예외 정보를 ErrorResponse로 포장하여 응답 생성
        val body = ErrorResponse(
            LocalDateTime.now().format(formatter),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            e.error,
            request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    // 비밀번호 예외 처리
    @ExceptionHandler(PasswordExceptions::class)
    fun handlePasswordExceptions(e: PasswordExceptions, request: WebRequest): ResponseEntity<ErrorResponse> {
        // 예외 정보를 ErrorResponse로 포장하여 응답 생성
        val body = ErrorResponse(
            LocalDateTime.now().format(formatter),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            e.error,
            request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    // 유효성 검증 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgNotValidException(
        e: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        // 유효성 검증 에러 메시지들을 결합하여 ErrorResponse 생성
        val fieldErrors = e.bindingResult.fieldErrors
        val errorMessages = fieldErrors.joinToString("; ") { fieldError ->
            val errorMessage = getErrorMessagesForValidationFailures(fieldError)
            errorMessage
        }

        // 예외 정보를 ErrorResponse로 포장하여 응답 생성
        val body = ErrorResponse(
            LocalDateTime.now().format(formatter),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            errorMessages,
            request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    // 유효성 검증 에러 코드에 해당하는 메시지 반환
    fun getErrorMessagesForValidationFailures(fieldError: FieldError): String {
        val errorCode = ValidationErrorCode.values().find { it.name == fieldError.code }
        return errorCode?.message ?: ValidationErrorCode.DEFAULT.message
    }
}