package suu.projekt.neo4j;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.List;

public class AdditionalSolution extends AbstractSolution {

    AdditionalSolution(Session session) {
        super(session);
    }

    public void databaseStatistics() {
        executeQuery("CALL db.labels()", this::databaseStatisticsExtractor);
        executeQuery("CALL db.relationshipTypes()", this::databaseStatisticsExtractor);
    }

    public void runAllTests() {
        executeQuery(newMovie("XYZ2"), null);
        executeQuery(newActorAndRelation("Jakub Nowak 2", "XYZ2", "ACTS_IN"), null);
        executeQuery(newActorAndRelation("Anna Kowalska 2", "XYZ2", "ACTS_IN"), null);
        executeQuery(actorsInMovie("XYZ2"), this::actorNameExtractor);
        executeQuery(howManyReviewsForMovies("Matrix"), this::howManyReviewsForMoviesExtractor);
        executeQuery(personalizedRecommendationsBasedOnGenres("Angelica Rodriguez"), this::personalizedRecommendationsBasedOnGenresExtractor);
    }

    private String newMovie(String movieTitle) {
        return "CREATE (n: Movie {title: '" + movieTitle + "'}) RETURN n";
    }

    private List<String> newActorAndRelation(String actorName, String movieTitle, String relation) {
        String queryActor = "CREATE (n: Actor {name: '" + actorName + "'}) RETURN n";
        String queryRelation = "MATCH (a: Actor), (m: Movie) WHERE a.name = '" + actorName + "' and m.title =" +
                "'" + movieTitle + "' CREATE (a)-[r: " + relation + "]->(m) RETURN r";

        return List.of(queryActor, queryRelation);
    }

    private String actorsInMovie(String movieTitle) {
        return "MATCH (m:Movie)<-[:ACTS_IN]-(a:Actor) " +
                "WHERE m.title CONTAINS \"" + movieTitle +"\" " +
                "RETURN a LIMIT 50";
    }

    private String actorSetProperties(String actorName, String birthplace, String birthdate) {
        return "MATCH (a: Actor) where a.name = '" + actorName +
                "' SET a.birthplace='" + birthplace + "', a.birthday='" + birthdate + "'";
    }

    private String howManyReviewsForMovies(String title) {
        return "MATCH (m:Movie)<-[:RATED]-(u:User) " +
                "WHERE m.title CONTAINS \"" + title + "\" " +
                "WITH m.title AS movie, COUNT(*) AS reviews " +
                "RETURN movie, reviews " +
                "ORDER BY reviews DESC " +
                "LIMIT 5;";
    }

    private String personalizedRecommendationsBasedOnGenres(String userName) {
        return "MATCH (u:User {name: \"" + userName + "\"})-[r:RATED]->(m:Movie), " +
                "  (m)-[:IN_GENRE]->(g:Genre)<-[:IN_GENRE]-(rec:Movie) " +
                "WHERE NOT EXISTS( (u)-[:RATED]->(rec) ) " +
                "WITH rec, [g.name, COUNT(*)] AS scores " +
                "RETURN rec.title AS recommendation, rec.year AS year, " +
                "COLLECT(scores) AS scoreComponents, " +
                "REDUCE (s=0,x in COLLECT(scores) | s+x[1]) AS score " +
                "ORDER BY score DESC LIMIT 10";
    }
}
