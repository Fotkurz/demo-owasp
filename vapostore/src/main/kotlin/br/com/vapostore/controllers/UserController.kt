package br.com.vapostore.controllers

import br.com.vapostore.models.dtos.NewUserRequest
import br.com.vapostore.repositories.UserRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Controller("/api/users")
@Validated
class UserController(@Inject val repository: UserRepository) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun create(@Body @Valid userRequest: NewUserRequest): HttpResponse<Any> {
        log.info("Creating new user")
        repository.save(userRequest.toModel())

        return HttpResponse.created(mapOf(Pair("message", "New user created")))
    }

}