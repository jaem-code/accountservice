package account.service

import account.dto.SignUpRequest
import account.entity.User

interface AuthService {
    // Sign Up 메서드
    fun registerUser(signUpRequest: SignUpRequest): User

    // 비밀번호 변경 메서드
    fun changePassword(email: String, currentPassword: String, newPassword: String): Boolean

    // 비밀번호가 악용된 비밀번호인지 확인
    fun isPasswordBreached(password: String): Boolean

    // 비밀번호가 유효한지 확인합니다.
    fun isPasswordValid(password: String): Boolean

    // 저장된 비밀번호 해시를 가져오는 로직
    fun getStoredPasswordHash(username: String): String?

    // 새로운 비밀번호가 유효한지 확인
    fun isNewPasswordValid(username: String, newPassword: String): Boolean

    // 저장된 비밀번호 해시를 업데이트
    fun updateStoredPasswordHash(username: String, newPassword: String)

}