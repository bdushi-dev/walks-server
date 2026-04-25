package al.bruno.walks.controller

import al.bruno.walks.model.Onboarding
import al.bruno.walks.service.OnboardingService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class OnboardingController(
    private val onboardingService: OnboardingService
) {
    @QueryMapping
    fun onboardings(
        @Argument locale: String
    ): Flux<Onboarding> = onboardingService.getOnboardings(locale = locale)
}