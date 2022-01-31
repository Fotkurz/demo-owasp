package br.com.vapostore.annotations

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [IsValidGenreValidation::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class IsValidGenre(
    val message: String = "Not a valid genre",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
