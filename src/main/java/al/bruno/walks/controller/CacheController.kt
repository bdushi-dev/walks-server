package al.bruno.walks.controller

import al.bruno.walks.config.CacheConfig
import org.springframework.cache.CacheManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Admin REST endpoints for cache management.
 * Protect these with Spring Security in production!
 */
/**
 * Admin REST endpoints for cache management.
 * Protect these with Spring Security in production!
 */
@RestController
@RequestMapping("/admin/cache")
class CacheController(private val cacheManager: CacheManager) {

    /** Evict all caches */
    @DeleteMapping
    fun evictAll(): ResponseEntity<Map<String, String>> {
        cacheManager.cacheNames.forEach { name ->
            cacheManager.getCache(name)?.clear()
        }
        return ResponseEntity.ok(mapOf("status" to "All caches evicted"))
    }

    /** Evict a specific cache by name */
    @DeleteMapping("/{name}")
    fun evictCache(@PathVariable name: String): ResponseEntity<Map<String, String>> {
        val cache = cacheManager.getCache(name)
            ?: return ResponseEntity.notFound().build()
        cache.clear()
        return ResponseEntity.ok(mapOf("status" to "Cache evicted: $name"))
    }

    /** Evict a specific key from a cache */
    @DeleteMapping("/{name}/{key}")
    fun evictKey(
        @PathVariable name: String,
        @PathVariable key: String
    ): ResponseEntity<Map<String, String>> {
        val cache = cacheManager.getCache(name)
            ?: return ResponseEntity.notFound().build()
        cache.evict(key)
        return ResponseEntity.ok(mapOf("status" to "Evicted key '$key' from cache: $name"))
    }

    /** List available cache names */
    @GetMapping
    fun listCaches(): ResponseEntity<Map<String, Any>> =
        ResponseEntity.ok(
            mapOf(
                "caches" to cacheManager.cacheNames,
                "availableCaches" to mapOf(
                    "tour" to CacheConfig.CACHE_TOUR,
                    "pointOfInterest" to CacheConfig.CACHE_POI,
                    "region" to CacheConfig.CACHE_REGION,
                    "category" to CacheConfig.CACHE_CATEGORY,
                    "audioGuide" to CacheConfig.CACHE_AUDIO_GUIDE,
                    "onboarding" to CacheConfig.CACHE_ONBOARDING,
                    "home" to CacheConfig.CACHE_HOME
                )
            )
        )
}
