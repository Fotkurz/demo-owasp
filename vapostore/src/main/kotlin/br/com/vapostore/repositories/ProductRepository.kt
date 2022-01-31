package br.com.vapostore.repositories

import br.com.vapostore.models.Genre
import br.com.vapostore.models.Product
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ProductRepository: JpaRepository<Product, String> {

    @Query("FROM Product p WHERE p.genre = :genre ORDER BY p.name")
    fun findAllByGenre(genre: Genre): List<Product>

    fun findByName(name: String): Product
}