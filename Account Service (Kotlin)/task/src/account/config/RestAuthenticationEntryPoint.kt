package account.config

import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/* Spring Security의 AuthenticationEntryPoint 인터페이스를 구현한 RestAuthenticationEntryPoint 클래스. 인증 예외가 발생했을 때 처리를 담당*/
@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    // commence 메서드: 인증 예외 처리
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        // HTTP 응답 상태 코드를 401 Unauthorized로 설정하고, 인증 예외의 메시지를 응답 본문에 포함하여 클라이언트에게 응답
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
    }
}