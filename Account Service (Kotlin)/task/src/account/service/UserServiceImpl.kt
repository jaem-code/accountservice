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

    override fun registerUser(signUpRequest: SignUpRequest): User {
        val existingUser = userRepository.findByEmailIgnoreCase(signUpRequest.email)
        if (existingUser != null) {
            throw UserAlreadyExistsException("User exist!")
        }

        // 이메일 중복 체크 등의 유효성 검사 수행 (필요한 경우)
        val encryptedPassword = passwordEncoder.encode(signUpRequest.password)

        val user = User(
            name = signUpRequest.name,
            lastname = signUpRequest.lastname,
            email = signUpRequest.email,
            password = encryptedPassword
        )

        return userRepository.save(user)
    }
}
