package al.bruno.walks.controller

import al.bruno.walks.model.Home
import al.bruno.walks.service.HomeService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class HomeController(
    private val homeService: HomeService
) {
    @QueryMapping
    fun home(
        @Argument id: String,
        @Argument locale: String
    ): Mono<Home> = homeService.getHome(
        id = id,
        locale = locale
    )

    @QueryMapping
    fun homes(
        @Argument locale: String
    ): Flux<Home> = homeService.getHomes(locale = locale)
}