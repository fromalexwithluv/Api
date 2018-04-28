package ca.switchboard.endpoint;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ValidationService {

    public void validateRequest(TopHitsRequest request, int depth) {
        if (request == null || request.getLog() == null || request.getLog().isEmpty() || depth < 1) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
