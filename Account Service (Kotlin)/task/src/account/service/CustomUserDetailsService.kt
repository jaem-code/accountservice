package account.service

import account.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
/* CustomUserDetailsService는 DB로부터 사용자 정보를 가져오고, 그 정보를 사용하여 CustomUserDetails 객체를 생성합니다.*/
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    // 사용자의 이메일을 기반으로 UserDetails를 조회하는 메서드
    // UserDetailsService 인터페이스를 구현하여 loadUserByUsername 메서드를 재정의
    // 입력된 이메일에 해당하는 사용자를 UserRepository를 통해 검색하고, 존재하지 않으면 예외 발생
    // 존재하는 사용자라면 CustomUserDetails 객체를 생성하여 반환
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmailIgnoreCase(username)
            ?: throw UsernameNotFoundException("User not found with email: $username")

        return CustomUserDetails(user) // CustomUserDetails는 사용자 정보를 담는 UserDetails 구현 클래스
    }
}

