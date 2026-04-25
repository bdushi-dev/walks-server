package al.bruno.walks.service

import al.bruno.walks.config.CacheConfig
import al.bruno.walks.graphql.Queries.tourById
import al.bruno.walks.graphql.Queries.toursByIds
import al.bruno.walks.model.Tour
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class TourService(
    private val graphQlClient: HttpGraphQlClient
) {
    @Cacheable(value = [CacheConfig.CACHE_TOUR], key = "#id")
    fun getTour(id: String, locale: String): Flux<Tour> {
        return graphQlClient.document(tourById(locale))
            .variable("id", id) // Ensure you pass the ID variable
            .retrieve("tour")
            // Use toEntityList for GraphQL arrays
            .toEntityList(Tour::class.java)
            // Convert Mono<List<Tour>> to Flux<Tour>
            .flatMapMany { Flux.fromIterable(it) }
            // Critical: .cache() prevents re-execution for new subscribers
            .cache()
    }

    @Cacheable(value = [CacheConfig.CACHE_TOUR], key = "#ids != null ? #ids.toString() : 'all'")
    fun getTours(ids: List<String>?, locale: String): Flux<Tour> {
        log.debug("Cache miss - fetching tours: {}", ids)
        return graphQlClient.document(toursByIds(locale))
            .variable("ids", ids ?: emptyList<String>())
            .retrieve("tourCollection.items") // Adjust path based on your Schema
            .toEntityList(Tour::class.java)
            .flatMapMany { Flux.fromIterable(it) }
            .cache()
    }

    companion object {
        private val log = LoggerFactory.getLogger(TourService::class.java)
    }
}