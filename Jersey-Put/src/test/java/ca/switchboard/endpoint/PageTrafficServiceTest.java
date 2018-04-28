package ca.switchboard.endpoint;

import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class PageTrafficServiceTest {

    private PageTrafficService processing;

    private ValidationService validation;
    private String logFilePath;
    private List<String> logEntries;
    private TopHitsRequest request;

    @Before
    public void setup() {
        processing = new PageTrafficService();
        validation = new ValidationService();
        logFilePath = "src/test/resources/valid-request-basic.txt";
        logEntries = parseLog(logFilePath);
        request = new TopHitsRequest(logEntries);
    }

    @Test(expected = WebApplicationException.class)
    public void testInvalidPathParam() {
        int numOfEdges = 0;
        validation.validateRequest(request, numOfEdges);
    }

    @Test(expected = WebApplicationException.class)
    public void testEmptyLog() {
        request = new TopHitsRequest(null);
        int numOfEdges = 3;
        validation.validateRequest(request, numOfEdges);
    }

    @Test
    public void testHappyPath() {
        int numOfEdges = 3;
        List<String> topHits = processing.computeTopHits(request, numOfEdges);
        assertEquals(2, topHits.size());
        assertThat(topHits, hasItems(
                "careers -> mission -> contact",
                "/ -> careers -> mission"
        ));
    }

    @Test
    public void testNo3PageVisits() {
        int numOfEdges = 3;
        logFilePath = "src/test/resources/valid-request-no-results.txt";
        logEntries = parseLog(logFilePath);
        request = new TopHitsRequest(logEntries);
        List<String> topHits = processing.computeTopHits(request, numOfEdges);
        assertEquals(0, topHits.size());
    }

    private static List<String> parseLog(String filePath) {
        List<String> logEntries = new LinkedList<>();
        if (filePath == null || filePath.isEmpty()) {
            return logEntries;
        }
        File f = new File(filePath);
        try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
            String currentLine = "";
            while ((currentLine = bf.readLine()) != null) {
                logEntries.add(currentLine);
            }
        } catch (IOException ex) {
            Logger.getLogger(PageTrafficServiceTest.class.getName()).log(Level.SEVERE, "File IO Error!", ex);
        }

        return logEntries;
    }
}
