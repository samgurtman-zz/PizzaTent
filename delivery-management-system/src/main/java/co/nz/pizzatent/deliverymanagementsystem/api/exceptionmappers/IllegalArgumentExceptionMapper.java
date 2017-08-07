package co.nz.pizzatent.deliverymanagementsystem.api.exceptionmappers;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException>{
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return new BadRequestException(exception).getResponse();
    }
}
