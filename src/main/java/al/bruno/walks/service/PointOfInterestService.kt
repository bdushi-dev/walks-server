package al.bruno.walks.service

import al.bruno.walks.config.CacheConfig
import al.bruno.walks.graphql.Queries.poiById
import al.bruno.walks.graphql.Queries.poiCollection
import al.bruno.walks.model.PointOfInterest
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PointOfInterestService(
    private val graphQlClient: HttpGraphQlClient
) {
    @Cacheable(value = [CacheConfig.CACHE_POI], key = "#id")
    fun getPointOfInterest(id: String, locale: String): Mono<PointOfInterest> {
        log.debug("Cache miss - fetching POI: {}", id)
        return graphQlClient.document(poiById(locale))
            .variable("id", id)
            .retrieve("pointOfInterest")
            .toEntity(PointOfInterest::class.java)
            .cache()
    }

    @Cacheable(value = [CacheConfig.CACHE_POI], key = "'list:' + #limit + ':' + (#categoryName ?: 'all')")
    fun getPointOfInterests(limit: Int?, categoryName: String, locale: String): Flux<PointOfInterest> {
        log.debug("Cache miss - fetching POIs limit={} category={}", limit, categoryName)
        return graphQlClient.document(poiCollection(locale))
            .variable("limit", limit ?: 50)
            .variable("categoryName", categoryName)
            .retrieve("pointOfInterestCollection.items")
            .toEntityList(PointOfInterest::class.java)
            .flatMapMany { Flux.fromIterable(it) }
            .cache()
    }

    companion object {
        private val log = LoggerFactory.getLogger(PointOfInterestService::class.java)
    }
}