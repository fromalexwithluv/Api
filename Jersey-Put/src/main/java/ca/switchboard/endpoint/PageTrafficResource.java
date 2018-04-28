package ca.switchboard.endpoint;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/PageTraffic")
public class PageTrafficResource {

    private static final Logger LOG = LoggerFactory.getLogger(PageTrafficResource.class);

    @PUT
    @Path("/{depth}/maxhits")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchPopularPaths(@PathParam("depth") int depth, TopHitsRequest request) {

        LOG.info("Validating client request...");
        ValidationService validationService = new ValidationService();
        validationService.validateRequest(request, depth);

        LOG.info("Finding {}-Page winner(s)...", depth);
        PageTrafficService processingService = new PageTrafficService();
        final List<String> mostVisitedUrls = processingService.computeTopHits(request, depth);

        return Response.status(Response.Status.OK).entity(mostVisitedUrls).build();
    }
}
