package al.bruno.walks.controller

import al.bruno.walks.model.AudioGuide
import al.bruno.walks.service.AudioGuideService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class AudioGuideController(
    private val audioGuideService: AudioGuideService
) {
    @QueryMapping
    fun audioGuide(
        @Argument id: String,
        @Argument locale: String
    ): Mono<AudioGuide> = audioGuideService.getAudioGuide(
        id = id,
        locale = locale
    )
}