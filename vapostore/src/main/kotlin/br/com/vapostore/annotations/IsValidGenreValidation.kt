package br.com.vapostore.annotations

import br.com.vapostore.models.Genre
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class IsValidGenreValidation: ConstraintValidator<IsValidGenre, String> {

    val log = LoggerFactory.getLogger(this.javaClass)

    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<IsValidGenre>,
        context: ConstraintValidatorContext
    ): Boolean {
        if ((value != null) && enumContains<Genre>(value)) {
            return true
        }

        log.info("Unknown genre: $value")
        context.messageTemplate("Unknown genre $value")
        return false
    }
}

inline fun <reified Genre : Enum<Genre>> enumContains(genre: String): Boolean {
    return enumValues<Genre>().any { it.name == genre}
}