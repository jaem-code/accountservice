package account.service

import account.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/* 생성된 CustomUserDetails 객체는 Spring Security에서 사용자의 인증 및 권한 정보를 관리하는 데 사용됩니다.*/
class CustomUserDetails(
    private val user: User // 사용자 정보를 담는 필드
) : UserDetails {

    // 사용자의 권한 정보를 반환하는 메서드
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList() // 권한 정보는 여기서 반환하며, 현재는 빈 리스트로 설정
    }

    // 사용자의 암호를 반환하는 메서드
    override fun getPassword(): String {
        return user.password
    }

    // 사용자의 식별자(이메일)을 반환하는 메서드
    override fun getUsername(): String {
        return user.email
    }

    // 사용자 계정이 만료되었는지 여부를 반환하는 메서드
    override fun isAccountNonExpired(): Boolean {
        return true // 계정 만료 여부에 따라 true 또는 false를 반환할 수 있음
    }

    // 사용자 계정이 잠겨있는지 여부를 반환하는 메서드
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    // 사용자의 인증 정보(비밀번호)가 만료되었는지 여부를 반환하는 메서드
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    // 사용자 계정이 활성화되었는지 여부를 반환하는 메서드
    override fun isEnabled(): Boolean {
        return true
    }

    // 추가: 사용자의 다른 정보들을 리턴하는 메서드들
    fun getId(): Long? {
        return user.id
    }

    fun getName(): String? {
        return user.name
    }

    fun getLastname(): String? {
        return user.lastname
    }

    fun getEmail(): String {
        return user.email
    }
}
