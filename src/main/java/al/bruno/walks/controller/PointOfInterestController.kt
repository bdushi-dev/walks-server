package al.bruno.walks.controller

import al.bruno.walks.model.PointOfInterest
import al.bruno.walks.service.PointOfInterestService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class PointOfInterestController(
    private val pointOfInterestService: PointOfInterestService,
) {
    @QueryMapping
    fun pointOfInterest(
        @Argument id: String,
        @Argument locale: String
    ): Mono<PointOfInterest> = pointOfInterestService.getPointOfInterest(
        id = id,
        locale = locale
    )

    @QueryMapping
    fun pointOfInterests(
        @Argument limit: Int?,
        @Argument categoryName: String,
        @Argument locale: String
    ): Flux<PointOfInterest> = pointOfInterestService.getPointOfInterests(
        limit = limit,
        categoryName = categoryName,
        locale = locale
    )
}