package ca.switchboard.endpoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PageTrafficService {

    public List<String> computeTopHits(TopHitsRequest request, int depth) {
        // to demonstrate, I use arbitrary names for identifiers

        // will eventually contain the winner(s)
        final List<String> topHits = new LinkedList<>();

        // a record of each user's most recent two-paged visit
        // key: user, value: 2-page url visits
        final Map<String, List<String>> usersBrowsingHistory = new HashMap<>();

        // a count of three-paged urls visits - the url(s) with the highest count win
        // key: url, value: visit count
        final Map<String, Integer> threePagePathsVisitCount = new HashMap<>();

        // presumed token separator
        final String whitespace = "\\s+";

        // assuming valid input
        for (String logEntry : request.getLog()) {

            // extract user and url values
            String[] tokens = logEntry.split(whitespace);
            if (tokens == null) {
                continue;
            }
            // used as a key in $usersBrowsingHistory
            String user = tokens[0];

            // stores a user's nth-page url visit 
            // it may be a page (single node) or url (multiple)
            String page = tokens[1];

            // stores a user's most recently visited (2-page) url  
            // stored in $usersBrowsingHistory
            List<String> twoPageUrls;

            // for new users, create new browsing history record  
            if (usersBrowsingHistory.get(user) == null) {
                twoPageUrls = new LinkedList<>();
            } else {
                // retrieve existing user's record
                twoPageUrls = usersBrowsingHistory.get(user);

                // if the url on record is 2-paged, append current
                // $page to the user's entry in $usersBrowsingHistory
                String threePagePath = "";
                String separator = " -> ";
                if (twoPageUrls.size() == (depth - 1)) {
                    for (int index = 0; index < (depth - 1); index++) {
                        threePagePath += twoPageUrls.get(index).trim() + separator;
                    }
                    threePagePath += page;
                } else if (twoPageUrls.size() > (depth - 1)) {
                    // remove 'offending' addition i.e. restrict storage to 2-paged urls only 
                    twoPageUrls.remove(0);
                    for (int index = 0; index < (depth - 1); index++) {
                        threePagePath += twoPageUrls.get(index).trim() + separator;
                    }
                    threePagePath += page;
                }

                // increment visit count for threePagePaths
                if (!threePagePath.isEmpty()) {
                    int count = (threePagePathsVisitCount.get(threePagePath) == null ? 0 : threePagePathsVisitCount.get(threePagePath)) + 1;
                    threePagePathsVisitCount.put(threePagePath, count);
                }
            }
            twoPageUrls.add(page);
            usersBrowsingHistory.put(user, twoPageUrls);
        } // end loop

        if (threePagePathsVisitCount.values().isEmpty()) {
            return topHits;
        }

        // find highest value in $threePagePathsVisitCount and add its corresponding key to $topHits
        int maxValueInMap = Collections.max(threePagePathsVisitCount.values());
        for (Entry<String, Integer> entry : threePagePathsVisitCount.entrySet()) {
            if (entry.getValue() == maxValueInMap) {
                topHits.add(entry.getKey());
            }
        }
        return topHits;
    }
}
