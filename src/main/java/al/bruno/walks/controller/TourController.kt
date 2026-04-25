package al.bruno.walks.controller

import al.bruno.walks.model.Tour
import al.bruno.walks.service.TourService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class TourController(
    private val tourService: TourService
) {
    @QueryMapping
    fun tour(
        @Argument id: String,
        @Argument locale: String
    ): Flux<Tour> = tourService.getTour(id = id, locale = locale)

    @QueryMapping
    fun tours(
        @Argument ids: List<String>?,
        @Argument locale: String
    ): Flux<Tour> = tourService.getTours(ids = ids, locale = locale)
}