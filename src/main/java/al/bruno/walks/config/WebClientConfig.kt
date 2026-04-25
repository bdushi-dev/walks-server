package al.bruno.walks.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(private val props: ContentfulProperties) {
    @Bean
    fun contentfulWebClient(): WebClient {
        return WebClient
            .builder()
            .baseUrl("https://graphql.contentful.com/content/v1/spaces/sh3vd5iqcfpg/environments/master")
//            .baseUrl(props.graphqlUrl)
//            .baseUrl("${props.graphqlUrl}/environments/${props.environment}")
            .defaultHeader("Authorization", "Bearer VuCXZOdLr07EhVC-fXqtUGdmzf5C7fmU96R3s162pqI")
            .defaultHeader("Content-Type", "application/json")
//            .codecs { it.defaultCodecs().maxInMemorySize(5 * 1024 * 1024) }
            .build()
    }

    companion object {
        private val log = LoggerFactory.getLogger(WebClientConfig::class.java)
    }
}
