package suu.projekt.neo4j;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.function.Function;

public class Solution extends AbstractSolution {

    Solution(Session session) {
        super(session);
    }

    public void databaseStatistics() {
        executeQuery("CALL db.labels()", this::databaseStatisticsExtractor);
        executeQuery("CALL db.relationshipTypes()", this::databaseStatisticsExtractor);
    }

    public void runAllTests() {
        executeQuery(findActorByName("Emma Watson"), this::actorNameExtractor);
        executeQuery(findMovieByTitleLike("Star Wars"), this::movieDataExtractor);
        executeQuery(findRatedMoviesForUser("Wayne Smith"), this::movieDataExtractor);
        executeQuery(findCommonMoviesForActors("Brad Pitt", "Angelina Jolie"), this::movieDataExtractor);
        executeQuery(findMovieRecommendationForUser("Wayne Smith"), this::movieDataExtractor);
    }

    private String findActorByName(final String actorName) {
        return "MATCH (a:Actor) WHERE a.name CONTAINS \"" + actorName + "\" RETURN a LIMIT 50";
    }

    private String findMovieByTitleLike(final String movieName) {
        return "MATCH (m:Movie) WHERE m.title CONTAINS \"" + movieName + "\" RETURN m LIMIT 50";
    }

    private String findRatedMoviesForUser(final String userName) {
        return "MATCH (m:Movie)<-[:RATED]-(u:User) WHERE u.name CONTAINS \"" + userName + "\" RETURN m LIMIT 10";
    }

    private String findCommonMoviesForActors(String actorOne, String actorTwo) {
        return "MATCH (a1:Actor)-[:ACTED_IN]->(m:Movie)<-[:ACTED_IN]-(a2:Actor) WHERE a1.name CONTAINS \"" +
                actorOne + "\" AND a2.name CONTAINS \"" + actorTwo + "\" RETURN m";
    }

    private String findMovieRecommendationForUser(final String userLogin) {
        return "MATCH (u1:User)-[rate:RATED]->(:Movie)<-[:RATED]-(u2:User)-[:RATED]->(m:Movie) " +
                "WHERE u1.name CONTAINS \"" + userLogin +
                "\" WITH m, u1, u2, count(rate) as Times " +
                "WHERE Times > 3 " +
                "RETURN u1, u2, m " +
                "LIMIT 50";
    }
}
