package br.com.vapostore.models

import br.com.vapostore.models.dtos.ProductResponse
import io.micronaut.data.annotation.AutoPopulated
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "products")
data class Product(
    @field:NotBlank val name: String,
    @field:NotNull
    @field:ManyToOne(optional = true, fetch = FetchType.LAZY)
    val publisher: User,
    @field:NotNull val price: BigDecimal,
    @field:NotBlank @field:Enumerated(EnumType.STRING) val genre: Genre
) {

    @Id
    var uuid: String = UUID.randomUUID().toString()

    var discountedPrice: BigDecimal = price
    var sale: Boolean = false

    fun startSale(discount: BigDecimal) {
        this.sale = true
        this.discountedPrice = price - (price * discount)
    }

    fun endSale() {
        this.sale = false
        this.discountedPrice = price
    }

    fun toDto(): ProductResponse {
        return ProductResponse(
            name = this.name,
            publisher = this.publisher.name,
            genre = this.genre.toString(),
            discountedPrice = this.discountedPrice,
            originalPrice = this.price
        )
    }
}