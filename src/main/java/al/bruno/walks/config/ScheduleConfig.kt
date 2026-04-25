package al.bruno.walks.config

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class ScheduleConfig {
    @Scheduled(fixedDelayString = "PT10M")
    @CacheEvict(
        value = [
            CacheConfig.CACHE_TOUR, CacheConfig.CACHE_POI, CacheConfig.CACHE_REGION,
            CacheConfig.CACHE_CATEGORY, CacheConfig.CACHE_AUDIO_GUIDE,
            CacheConfig.CACHE_ONBOARDING, CacheConfig.CACHE_HOME
        ],
        allEntries = true
    )
    fun evictAllCaches() {
        log.info("Scheduled cache eviction complete")
    }

    companion object {
        private val log = LoggerFactory.getLogger(ScheduleConfig::class.java)
    }
}