package br.com.vapostore.security

import br.com.vapostore.models.Role
import br.com.vapostore.repositories.UserRepository
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.token.RolesFinder
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.util.*
import java.util.stream.Stream
import kotlin.math.log
import kotlin.streams.toList

@Singleton
class Authenticator(@Inject val repository: UserRepository,
                    @Inject val encoder: PasswordEncoder): AuthenticationProvider {

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>?
    ): Publisher<AuthenticationResponse> {

        val login = authenticationRequest!!.identity as String
        val password = authenticationRequest!!.secret as String

        val user = repository.findByLogin(login).get()

        return Flux.create ({ emitter: FluxSink<AuthenticationResponse> ->
            val isSamePw = encoder.compare(password, user.password, user.salt)
            if(isSamePw) {
                val roles: List<String> = user.roles.map {
                    it.toString()
                }

                emitter.next(AuthenticationResponse.success(authenticationRequest.identity as String, roles))
                emitter.complete()
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        }, FluxSink.OverflowStrategy.ERROR)
    }

}