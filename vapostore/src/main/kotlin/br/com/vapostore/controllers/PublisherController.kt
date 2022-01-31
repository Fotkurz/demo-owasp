package br.com.vapostore.controllers

import br.com.vapostore.repositories.UserRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import javax.transaction.Transactional

@Controller("/api/publishers")
class PublisherController(@Inject val userRepository: UserRepository) {

    val log = LoggerFactory.getLogger(this.javaClass)

    @Put
    @Secured(value = ["ROLE_ADMIN", "ROLE_CLIENT", "ROLE_PUBLISHER"])
    fun becomePublisher(authentication: Authentication): HttpResponse<Any> {
        val optionalUser = userRepository.findByLogin(authentication.name)

        if(optionalUser.isEmpty) return HttpResponse.notFound(mapOf(Pair("message", "Not a valid user")))

        val user = optionalUser.get()
        log.info("Activating Publisher status to ${user.name}")

        if(user.isPublisher) return HttpResponse.badRequest(mapOf(Pair("message", "user is a publisher already")))

        user.becomePublisher()

        userRepository.update(user)
        userRepository.flush()

        return HttpResponse.ok()
    }


}