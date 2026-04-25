package al.bruno.walks.service

import al.bruno.walks.config.CacheConfig
import al.bruno.walks.graphql.Queries.onboardingCollection
import al.bruno.walks.model.Onboarding
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class OnboardingService(
    private val graphQlClient: HttpGraphQlClient
) {
    @Cacheable(value = [CacheConfig.CACHE_ONBOARDING], key = "'all'")
    fun getOnboardings(locale: String): Flux<Onboarding> {
        log.debug("Cache miss - fetching onboardings")
        return graphQlClient.document(onboardingCollection(locale))
            .retrieve("onboardingCollection.items")
            .toEntityList(Onboarding::class.java)
            .flatMapMany { Flux.fromIterable(it) }
            .cache()
    }

    companion object {
        private val log = LoggerFactory.getLogger(OnboardingService::class.java)
    }
}