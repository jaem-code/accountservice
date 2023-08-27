package account.service

import account.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: User // 다른 필드들도 추가 가능
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true // 계정 만료 여부에 따라 true 또는 false 반환
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
    // 다른 메서드들도 필요에 따라 구현 가능

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