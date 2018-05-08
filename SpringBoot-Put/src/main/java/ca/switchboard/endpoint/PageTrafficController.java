package ca.switchboard.endpoint;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/PageTraffic")
public class PageTrafficController {

    private final AtomicLong counter = new AtomicLong();
    private static final Logger LOG = LoggerFactory.getLogger(PageTrafficController.class);

    @Resource
    PageTrafficService processingService;

    @Resource
    ValidationService validationService;

    @RequestMapping(value = "/{depth}/maxhits", method = RequestMethod.PUT)
    public List<String> fetchPopularPaths(@PathVariable int depth, @RequestBody TopHitsRequest request) throws BadRequestException {

        LOG.info("Validating client request: {} ", counter.incrementAndGet());
        validationService.validateRequest(request, depth);

        LOG.info("Finding {}-Page winner(s)...", depth);
        final List<String> mostVisitedUrls = processingService.computeTopHits(request, depth);
        return mostVisitedUrls;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Request")
    @ExceptionHandler({BadRequestException.class})
    public void handleBadRequestException() {
    }
}
