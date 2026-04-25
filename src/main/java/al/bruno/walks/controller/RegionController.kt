package al.bruno.walks.controller

import al.bruno.walks.model.Region
import al.bruno.walks.service.RegionService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class RegionController(
    private val regionService: RegionService,
) {
    @QueryMapping
    fun region(
        @Argument id: String,
        @Argument locale: String
    ): Mono<Region> = regionService.getRegion(
        id = id,
        locale = locale
    )

    @QueryMapping
    fun regions(
        @Argument locale: String
    ): Flux<Region> = regionService.getRegions(locale = locale)
}