package account.repository

import account.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    // 이메일을 대소문자 구분 없이 검색하여 사용자 정보를 가져옵니다.
    fun findByEmailIgnoreCase(email: String): User?

    // 비밀번호 업데이트를 위한 쿼리 메서드입니다.
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :email")
    fun updatePassword(@Param("email") email: String, @Param("newPassword") newPassword: String)
}