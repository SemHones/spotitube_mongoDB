package services.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TokenExceptionMapper implements ExceptionMapper<TokenException> {

    @Override
    public Response toResponse(TokenException e) {
        return Response.status(403).build();
    }
}
