package br.com.vapostore.repositories

import br.com.vapostore.models.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, String> {

    fun findByLogin(login: String): Optional<User>
}
