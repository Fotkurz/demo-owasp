package br.com.vapostore.models.dtos

import br.com.vapostore.annotations.IsValidGenre
import br.com.vapostore.models.Genre
import br.com.vapostore.models.Product
import br.com.vapostore.models.User
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.NotBlank

@Introspected
open class NewProductRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val price: BigDecimal   ,
    @field:NotBlank @field:IsValidGenre val genre: String
) {

    val discount: Double = 0.0

    fun toModel(publisher: User): Product {
        return Product(
            this.name,
            publisher,
            this.price,
            Genre.valueOf(this.genre)
        )
    }
}
