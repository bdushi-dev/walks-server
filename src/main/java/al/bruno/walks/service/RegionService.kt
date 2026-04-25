package al.bruno.walks.service

import al.bruno.walks.config.CacheConfig
import al.bruno.walks.graphql.Queries.regionById
import al.bruno.walks.graphql.Queries.regionCollection
import al.bruno.walks.model.Region
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RegionService(
    private val graphQlClient: HttpGraphQlClient
) {
    @Cacheable(value = [CacheConfig.CACHE_REGION], key = "#id")
    fun getRegion(id: String, locale: String): Mono<Region> {
        log.debug("Cache miss - fetching region: {}", id)
        return graphQlClient.document(regionById(locale))
            .variable("id", id)
            .retrieve("region")
            .toEntity(Region::class.java)
            .cache()
    }

    @Cacheable(value = [CacheConfig.CACHE_REGION], key = "'all'")
    fun getRegions(locale: String): Flux<Region> {
        log.debug("Cache miss - fetching all regions")
        return graphQlClient.document(regionCollection(locale))
            .retrieve("regionCollection.items")
            .toEntityList(Region::class.java)
            .map { it }
            .flatMapMany { Flux.fromIterable(it) }
            .cache()
    }

    companion object {
        private val log = LoggerFactory.getLogger(RegionService::class.java)
    }
}