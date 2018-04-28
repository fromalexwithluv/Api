package ca.switchboard.endpoint;

import java.util.LinkedList;
import java.util.List;

public class TopHitsRequest {

    List<String> log = new LinkedList();

    public TopHitsRequest() {
    }

    public TopHitsRequest(List<String> log) {
        this.log = log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public List<String> getLog() {
        return log;
    }
}
