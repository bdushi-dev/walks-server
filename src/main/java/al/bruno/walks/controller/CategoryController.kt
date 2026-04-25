package al.bruno.walks.controller

import al.bruno.walks.model.Category
import al.bruno.walks.service.CategoryService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class CategoryController(
    private val categoryService: CategoryService
) {
    @QueryMapping
    fun category(
        @Argument id: String,
        @Argument locale: String
    ): Mono<Category> = categoryService.getCategory(
        id = id,
        locale = locale
    )
    @QueryMapping
    fun categories(
        @Argument locale: String
    ): Flux<Category> = categoryService.getCategories(locale = locale)
}