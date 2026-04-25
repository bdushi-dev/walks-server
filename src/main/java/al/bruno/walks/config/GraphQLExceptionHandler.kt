package al.bruno.walks.config

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component

@Component
class GraphQLExceptionHandler : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        if (ex is RuntimeException && ex.message != null && ex.message!!.startsWith("Contentful returned errors")) {
            return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Upstream data error: " + ex.message)
                .build()
        }

        return GraphqlErrorBuilder.newError(env)
            .errorType(ErrorType.INTERNAL_ERROR)
            .message("An internal error occurred. Please try again.")
            .build()
    }
}
