package br.com.vapostore.models

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.AutoPopulated
import org.hibernate.annotations.Fetch
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "users")
data class User(
    @field:NotBlank val login: String,
    @field:NotBlank val password: String,
    @field:NotBlank val name: String,
    @field:NotBlank @field:Email val email: String
) {
    @Id
    var uuid = UUID.randomUUID().toString()

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @OrderColumn
    var roles: Array<Role> = arrayOf(Role.ROLE_CLIENT)

    var isPublisher: Boolean = false

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_uuid")
    lateinit var ownedProducts: List<Product>

    lateinit var salt: ByteArray

    fun becomePublisher() {
        this.isPublisher = true
        this.roles = this.roles.plus(Role.ROLE_PUBLISHER)
    }

    override fun toString(): String {
        return "{\"email\":\"$email\"}"
    }
}