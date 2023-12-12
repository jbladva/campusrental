package com.campusrental.exception.handler;

import com.campusrental.exception.CapacityExceededException;
import com.campusrental.exception.PropertyNotFoundException;
import com.campusrental.exception.PropertyValidationException;
import com.campusrental.exception.UserUnAuthorizedException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof PropertyNotFoundException)
        {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .build();
        } else if (ex instanceof CapacityExceededException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .build();

        }
        else if (ex instanceof PropertyValidationException){
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .build();
        }
        else if (ex instanceof UserUnAuthorizedException){
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .build();
        }
            else return null;
    }
}
