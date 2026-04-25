package al.bruno.walks.service

import al.bruno.walks.config.CacheConfig
import al.bruno.walks.graphql.Queries.audioGuideById
import al.bruno.walks.model.AudioGuide
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AudioGuideService(
    private val graphQlClient: HttpGraphQlClient
) {
    @Cacheable(value = [CacheConfig.CACHE_AUDIO_GUIDE], key = "#id")
    fun getAudioGuide(id: String, locale: String): Mono<AudioGuide> {
        log.debug("Cache miss - fetching audio guide: {}", id)
        return graphQlClient.document(audioGuideById(locale))
            .variable("id", id)
            .retrieve("audioGuide")
            .toEntity(AudioGuide::class.java)
            .cache()
    }
    companion object {
        private val log = LoggerFactory.getLogger(AudioGuideService::class.java)
    }
}