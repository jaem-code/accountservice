package account.service

import account.dto.SignUpRequest
import account.entity.User
import account.exception.UserAlreadyExistsException
import account.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserService {

    // 회원 가입을 처리하는 메서드
    override fun registerUser(signUpRequest: SignUpRequest): User {
        // 이미 등록된 이메일인지 검사
        val existingUser = userRepository.findByEmailIgnoreCase(signUpRequest.email)
        if (existingUser != null) {
            throw UserAlreadyExistsException("User exist!")
        }

        // 패스워드 암호화 먼저하고 회원 정보 생성. password를 reassign 할 수 없기 때문
        val encryptedPassword = passwordEncoder.encode(signUpRequest.password)

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
}

