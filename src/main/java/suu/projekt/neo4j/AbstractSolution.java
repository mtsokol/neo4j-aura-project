package suu.projekt.neo4j;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.List;
import java.util.function.Function;

abstract public class AbstractSolution {

    protected final Session session;

    AbstractSolution(Session session) {
        this.session = session;
    }

    protected void executeQuery(final String query, Function<Record, String> dataExtractor) {
        System.out.println("Executing query: " + query);
        StatementResult result = session.run(query);
        if (dataExtractor != null) {
            while (result.hasNext()) {
                System.out.println(dataExtractor.apply(result.next()));
            }
        }
    }

    protected void executeQuery(final List<String> queryList, Function<Record, String> dataExtractor) {
        queryList.forEach(query -> executeQuery(query, dataExtractor));
    }

    protected String databaseStatisticsExtractor(Record record) {
        return record.toString();
    }

    protected String actorNameExtractor(Record record) {
        return record.get("a").get("name").toString();
    }

    protected String movieDataExtractor(Record record) {
        return String.format("%s (%s)", record.get("m").get("title"), record.get("m").get("year"));
    }

    protected String howManyReviewsForMoviesExtractor(Record record) {
        return String.format("%s (%s)", record.get("movie"), record.get("reviews"));
    }

    protected String personalizedRecommendationsBasedOnGenresExtractor(Record record) {
        return String.format("%s (%s)", record.get("recommendation"), record.get("year"));
    }
}
