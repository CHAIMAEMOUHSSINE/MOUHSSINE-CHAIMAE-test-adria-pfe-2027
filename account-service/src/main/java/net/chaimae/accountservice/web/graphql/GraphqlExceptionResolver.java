package net.chaimae.accountservice.web.graphql;


import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

import net.chaimae.accountservice.Account.AccountNotFoundException;
import net.chaimae.accountservice.Account.InsufficientFundsException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof AccountNotFoundException) {
            return build(ex, env, ErrorType.NOT_FOUND);
        }
        if (ex instanceof InsufficientFundsException || ex instanceof IllegalArgumentException) {
            return build(ex, env, ErrorType.BAD_REQUEST);
        }
        return null;
    }

    private GraphQLError build(Throwable ex, DataFetchingEnvironment env, ErrorType type) {
        return GraphqlErrorBuilder.newError(env).errorType(type).message(ex.getMessage()).build();
    }
}
