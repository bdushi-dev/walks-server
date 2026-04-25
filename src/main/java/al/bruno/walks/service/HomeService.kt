package al.bruno.walks.service

import al.bruno.walks.config.CacheConfig
import al.bruno.walks.graphql.Queries.homeById
import al.bruno.walks.graphql.Queries.homeCollection
import al.bruno.walks.model.Home
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class HomeService(
    private val graphQlClient: HttpGraphQlClient
) {
    @Cacheable(value = [CacheConfig.CACHE_HOME], key = "#id")
    fun getHome(id: String, locale: String): Mono<Home> {
        log.debug("Cache miss - fetching home: {}", id)
        return graphQlClient.document(homeById(locale))
            .variable("id", id)
            .retrieve("home")
            .toEntity(Home::class.java)
            .cache()
    }

    @Cacheable(value = [CacheConfig.CACHE_HOME], key = "'all'")
    fun getHomes(locale: String): Flux<Home> {
        return graphQlClient.document(homeCollection(locale))
            .retrieve("homeCollection.items")
            .toEntityList(Home::class.java)
            .flatMapMany { Flux.fromIterable(it) }
            .doOnSubscribe { log.debug("Fetching homes") }
            .doOnError { log.error("Error fetching homes: {}", it.message, it) }
            .onErrorResume { Flux.empty() }
    }

    companion object {
        private val log = LoggerFactory.getLogger(HomeService::class.java)
    }
}