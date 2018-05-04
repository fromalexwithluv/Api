package ca.switchboard.endpoint;

import org.springframework.stereotype.Component;

@Component
public class ValidationService {

    public void validateRequest(TopHitsRequest request, int depth) throws BadRequestException {
        if (request == null || request.getLog() == null || request.getLog().isEmpty() || depth < 1) {
            throw new BadRequestException();
        }
    }
}
