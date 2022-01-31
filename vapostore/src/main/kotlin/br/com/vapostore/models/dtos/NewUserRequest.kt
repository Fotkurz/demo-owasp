package br.com.vapostore.models.dtos

import br.com.vapostore.models.User
import br.com.vapostore.security.PasswordEncoder
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Introspected
class NewUserRequest(
    @field:NotBlank val login: String,
    @field:NotBlank val password: String,
    @field:NotBlank val name: String,
    @field:NotBlank @field:Email val email: String
) {

    fun toModel(): User {
        val pe: PasswordEncoder = PasswordEncoder()
        val salt = pe.saltGen()

        val hashed: String = pe.encode(password, salt)

        val user: User = User(
            login = this.login,
            password = hashed,
            name = this.name,
            email = this.email
        )

        user.salt = salt

        return user
    }

}
