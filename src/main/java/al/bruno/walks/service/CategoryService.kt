package al.bruno.walks.service

import al.bruno.walks.config.CacheConfig
import al.bruno.walks.graphql.Queries.categoryCollection
import al.bruno.walks.model.Category
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CategoryService(
    private val graphQlClient: HttpGraphQlClient
) {
    @Cacheable(value = [CacheConfig.CACHE_CATEGORY], key = "'all'")
    fun getCategories(locale: String): Flux<Category> {
        log.debug("Cache miss - fetching categories: {}", locale)
        return graphQlClient.document(categoryCollection(locale))
            .retrieve("categoryCollection.items")
            .toEntityList(Category::class.java)
            .flatMapMany { Flux.fromIterable(it) }
            .cache()
    }

    @Cacheable(value = [CacheConfig.CACHE_CATEGORY], key = "#id")
    fun getCategory(id: String, locale: String): Mono<Category> {
        log.debug("Cache miss - fetching category by id: {} {}", id, locale)
        return getCategories(locale)
            .filter { it.id == id }
            .next()
            .switchIfEmpty(Mono.error(RuntimeException("Category not found: $id")))
            .cache()
    }
    companion object {
        private val log = LoggerFactory.getLogger(AudioGuideService::class.java)
    }
}