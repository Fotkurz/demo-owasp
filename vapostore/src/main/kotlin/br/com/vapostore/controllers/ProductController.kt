package br.com.vapostore.controllers

import br.com.vapostore.models.Genre
import br.com.vapostore.models.Product
import br.com.vapostore.models.User
import br.com.vapostore.models.dtos.NewProductRequest
import br.com.vapostore.models.dtos.ProductListResponse
import br.com.vapostore.repositories.ProductRepository
import br.com.vapostore.repositories.UserRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import javax.validation.Valid

@Controller("/api/products")
@Validated
class ProductController(
    @Inject val repository: ProductRepository,
    @Inject val userRepository: UserRepository
    ) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * List all products when HTTP GET
     *
     * @return The list of all registred products
     */
    @Get(produces = [MediaType.APPLICATION_JSON])
    @Transactional
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun list(): HttpResponse<Any> {
        log.info("Listing all products")

        val productList = repository.findAll()
        if(productList.isEmpty()) return HttpResponse.notFound(mapOf(Pair("message", "No products found")))

        return HttpResponse.ok(ProductListResponse(productList.map(Product::toDto).toList()))
    }

    /**
     * List all products by genre when HTTP GET
     *
     * @param genre The genre of the products for search
     * @return HttpStatus OK and a List of products with a specific genre
     */
    @Get(value = "/{genre}", produces = [MediaType.APPLICATION_JSON])
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun listByName(@PathVariable genre: String): HttpResponse<Any> {
        log.info("Fetching all titles with genre: $genre")

        if(!enumContains<Genre>(genre)) {
            log.info("Unknown genre: $genre")
            return HttpResponse.badRequest(mapOf(Pair("message", "Genre not found")))
        }

        val productList: List<Product> = repository.findAllByGenre(Genre.valueOf(genre))

        if(productList.isEmpty()) {
            log.info("No products of the genre: $genre")
            return HttpResponse.ok(mapOf(Pair("message", "No products found with this genre: $genre")))
        }

        log.info("Listing all products with genre: $genre")
        return HttpResponse.ok(
            ProductListResponse(
                productList.map(Product::toDto).toList()
            ))
    }

    
    /**
     * Create a new product bound to User (Publisher) found by the Token
     *
     * @param {ProductRequest} productRequest The request DTO do create a new Product
     * @return HttpStatus CREATED and a JSON with the UUID of the created product
     */
    @Post(produces = [MediaType.APPLICATION_JSON])
    @Secured("ROLE_ADMIN", "ROLE_PUBLISHER")
    fun create(@Body @Valid productRequest: NewProductRequest, authentication:Authentication): HttpResponse<Any> {
        log.info("Creating new product")

        log.info(authentication.name)

        val optionalUser: Optional<User> = userRepository.findByLogin(authentication.name)

        if(optionalUser.isEmpty) {
            return HttpResponse.notFound()
        }
        val user: User = optionalUser.get()
        val newProduct = productRequest.toModel(user)

        repository.save(newProduct)
        return HttpResponse.created(mapOf(Pair("message", "Product [${newProduct.name}] created")))
    }

    /**
     * Put product on sale based on discount price.
     *
     * @param {String} productName - The name of the Product to go into discount.
     * @return {HttpResponse<Any>} HttpResponse<Any> - A HttpResponse
     */
    @Put("/{id}")
    @Secured("ROLE_ADMIN", "ROLE_PUBLISHER")
    fun sale(@PathVariable id: String, discount: BigDecimal, authentication: Authentication): HttpResponse<Any> {
        log.info("Applying sale on product $id")

        val optionalUser = userRepository.findByLogin(authentication.name)
        val optionalProduct = repository.findById(id)

        if(optionalProduct.isEmpty) return HttpResponse.notFound(mapOf(Pair("message", "No product $id")))

        val product: Product = optionalProduct.get()
        log.info(Float.MIN_VALUE.toString())
        if(discount <= BigDecimal.ZERO) {
            log.info("User ${authentication.name} ending sale on (${product.name})")
            product.endSale()
        } else {
            log.info("User ${authentication.name} starting sale for (${product.name})")
            product.startSale(discount = discount)
        }

        repository.update(product)

        return HttpResponse.ok(mapOf(Pair("message", "Product $id is on sale")))
    }



}

inline fun <reified Genre : Enum<Genre>> enumContains(genre: String): Boolean {
    return enumValues<Genre>().any { it.name == genre}
}