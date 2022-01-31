package br.com.vapostore.models.dtos

import br.com.vapostore.models.Genre
import br.com.vapostore.models.Product

class ProductListResponse(
    val list: List<ProductResponse>
) {

    fun toResponse(productList: List<Product>): ProductListResponse {
        return ProductListResponse(productList.map(Product::toDto).toList())
    }
}