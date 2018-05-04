package ca.switchboard.endpoint;

public class ValidationService {

    public void validateRequest(TopHitsRequest request, int depth) throws BadRequestException {
        if (request == null || request.getLog() == null || request.getLog().isEmpty() || depth < 1) {
            throw new BadRequestException();
        }
    }
}
