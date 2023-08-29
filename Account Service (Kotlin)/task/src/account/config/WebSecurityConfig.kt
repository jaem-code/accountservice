package account.config

import account.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: CustomUserDetailsService,
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint
) {

    // SecurityFilterChain을 정의하는 빈 설정
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .exceptionHandling { ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint) } // 예외 처리 설정
            .csrf { csrf -> csrf.disable() }  // CSRF 보호 비활성화 (Postman을 위해)
            .headers { headers -> headers.frameOptions().disable() } // X-Frame-Options 비활성화 (H2 콘솔을 위해)
            .authorizeRequests { auth ->
                auth
                    .antMatchers("/h2-console/**").permitAll() // H2 콘솔 접근 허용
                    .antMatchers("/actuator/shutdown").permitAll() //
                    .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                    .antMatchers("/api/empl/payment").authenticated() // /api/empl/payment은 인증된 사용자만 허용 // /api/signup은 인증 없이 허용
                    .anyRequest().authenticated() // 나머지 요청은 인증된 사용자만 허용
            }
            .sessionManagement { sessions ->
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화 (no session)
            }
            .authenticationProvider(daoAuthenticationProvider()) // DaoAuthenticationProvider 설정
            .httpBasic(Customizer.withDefaults()) // HTTP 기본 인증 사용 설정
            // SecurityFilterChain 설정 종료
            .build()

    // REST API에 대한 인증 예외 처리를 위한 빈 설정
//    @Bean
//    fun restAuthenticationEntryPoint(): AuthenticationEntryPoint {
//        return RestAuthenticationEntryPoint()
//    }

    // DaoAuthenticationProvider 설정을 위한 빈 설정
    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder()) // PasswordEncoder 설정 추가
        return authProvider
    }

    // 비밀번호 암호화를 위한 PasswordEncoder 빈 설정
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(13) // BCrypt 암호화 방식 사용
    }
}
