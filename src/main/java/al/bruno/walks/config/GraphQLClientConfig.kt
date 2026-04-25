package al.bruno.walks.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GraphQLClientConfig(
    private val contentfulWebClient: WebClient
) {
    @Bean
    fun httpGraphQlClient(): HttpGraphQlClient {
        return HttpGraphQlClient.create(contentfulWebClient)
    }
}