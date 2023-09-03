package account.service

import account.dto.SignUpRequest
import account.entity.User
import account.exception.UserAlreadyExistsException
import account.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
): AuthService{
    // 회원 가입을 처리하는 메서드
    override fun registerUser(signUpRequest: SignUpRequest): User {
        // 이미 등록된 이메일인지 검사
        val existingUser = userRepository.findByEmailIgnoreCase(signUpRequest.email)
        if (existingUser != null) {
            throw UserAlreadyExistsException("User exist!")
        }

        // 패스워드 암호화 먼저하고 회원 정보 생성. password를 reassign 할 수 없기 때문
        val encryptedPassword =encoder.encode(signUpRequest.password)

        // 회원 정보 생성
        val user = User(
            name = signUpRequest.name,
            lastname = signUpRequest.lastname,
            email = signUpRequest.email,
            password = encryptedPassword
        )

        // 회원 정보 저장 후 반환
        return userRepository.save(user)
    }


    // 비밀번호 변경 메서드
   override fun changePassword(email: String, currentPassword: String, newPassword: String): Boolean {
        val user = userRepository.findByEmailIgnoreCase(email) ?: return false

        // 현재 비밀번호가 저장된 해시와 일치하는지 확인
        if (!encoder.matches(currentPassword, user.password)) {
            return false
        }

        val newPasswordHash = encoder.encode(newPassword)
        updateStoredPasswordHash(email, newPasswordHash)

        return true
    }

    // 악용된 비밀번호 목록 초기화
    private val breachedPasswords: Set<String> = loadBreachedPasswords()

    // 악용된 비밀번호 목록을 초기화합니다.
    private fun loadBreachedPasswords(): Set<String> {
        // 외부 리소스(파일, 데이터베이스 등)에서 악용된 비밀번호를 불러옵니다.
        // 악용된 비밀번호 목록을 반환합니다.
        return setOf(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
        )
    }

    // 비밀번호가 악용된 비밀번호인지 확인합니다.
    override fun isPasswordBreached(password: String): Boolean {
        return breachedPasswords.contains(password)
    }

    // 비밀번호가 유효한지 확인합니다.
    override fun isPasswordValid(password: String): Boolean {
        return password.length >= 12 && !breachedPasswords.contains(password)
    }

    // 저장된 비밀번호 해시를 가져오는 로직을 구현하세요
    override fun getStoredPasswordHash(username: String): String? {
        return userRepository.findByEmailIgnoreCase(username)?.password
    }

    // 새로운 비밀번호가 유효한지 확인합니다.
    override fun isNewPasswordValid(username: String, newPassword: String): Boolean {
        val storedPasswordHash = getStoredPasswordHash(username)
        return storedPasswordHash != null && encoder.matches(newPassword, storedPasswordHash)
    }

    // 저장된 비밀번호 해시를 업데이트합니다.
    override fun updateStoredPasswordHash(username: String, newPassword: String) {
        if (isPasswordValid(newPassword)) {
            val newHash = encoder.encode(newPassword)
            userRepository.updatePassword(username, newHash)
        }
    }
}