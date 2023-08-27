package account.service

import account.dto.SignUpRequest
import account.entity.User

interface UserService {
    fun registerUser(signUpRequest: SignUpRequest): User
}

