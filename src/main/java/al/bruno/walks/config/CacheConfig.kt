package al.bruno.walks.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheConfig {
    companion object {
        const val CACHE_TOUR        = "tour"
        const val CACHE_POI         = "pointOfInterest"
        const val CACHE_REGION      = "region"
        const val CACHE_CATEGORY    = "category"
        const val CACHE_AUDIO_GUIDE = "audioGuide"
        const val CACHE_ONBOARDING  = "onboarding"
        const val CACHE_HOME        = "home"
    }

    @Bean
    fun cacheManager(): CacheManager =
        CaffeineCacheManager(
            CACHE_TOUR,
            CACHE_POI,
            CACHE_REGION,
            CACHE_CATEGORY,
            CACHE_AUDIO_GUIDE,
            CACHE_ONBOARDING,
            CACHE_HOME
        )
            .apply {
                setCaffeine(defaultSpec())
                setAsyncCacheMode(true) // ✅ THIS is what you’re missing
            }

    private fun defaultSpec(): Caffeine<Any, Any> =
        Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
}