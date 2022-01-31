package br.com.vapostore.models.dtos

import br.com.vapostore.models.Product
import java.math.BigDecimal

class ProductResponse(
    val name: String,
    val publisher: String,
    val genre: String,
    val originalPrice: BigDecimal,
    val discountedPrice: BigDecimal
) {

    fun toResponse(product: Product): ProductResponse {
        return ProductResponse(
            name = product.name,
            publisher = product.publisher.name,
            genre = product.genre.toString(),
            originalPrice = product.price,
            discountedPrice = product.discountedPrice
        )
    }
}
