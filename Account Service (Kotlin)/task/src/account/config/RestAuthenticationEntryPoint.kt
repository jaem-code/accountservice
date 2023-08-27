package account.config

import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.core.AuthenticationException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
    }
}