package com.bookstore.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {
    @Override
    public Response toResponse(InvalidInputException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(exception.getMessage(), 400))
                .build();
    }
}