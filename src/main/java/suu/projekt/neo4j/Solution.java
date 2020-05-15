package suu.projekt.neo4j;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Solution extends AbstractSolution {

    Solution(Session session) {
        super(session);
    }

    public void databaseStatistics() {
        StatementResult res1 = session.run("CALL db.labels()");
        while (res1.hasNext()) {
            System.out.println(res1.next());
        }
        StatementResult res2 = session.run("CALL db.relationshipTypes()");
        while (res2.hasNext()) {
            System.out.println(res2.next());
        }
    }

    public void runAllTests() {
        findActorByName("Emma Watson");
        findMovieByTitleLike("Star Wars");
        findRatedMoviesForUser("maheshksp");
        findCommonMoviesForActors("Emma Watson", "Daniel Radcliffe");
        findMovieRecommendationForUser("emileifrem");
    }

    private void findActorByName(final String actorName) {
        StatementResult res = session.run("MATCH (a:Actor) WHERE a.name CONTAINS \"" + actorName + "\" RETURN a LIMIT 50");
        while (res.hasNext()) {
            System.out.println(res.next().get("a").get("name"));
        }
    }

    private void findMovieByTitleLike(final String movieName) {
        StatementResult res = session.run("MATCH (m:Movie) WHERE m.title CONTAINS \"" + movieName + "\" RETURN m LIMIT 50");
        while (res.hasNext()) {
            System.out.println(res.next().get("m").keys());
        }
    }

    private void findRatedMoviesForUser(final String userLogin) {
        StatementResult res = session.run("MATCH (m:Movie)<-[:RATED]-(u:User) WHERE u.name CONTAINS \"" + userLogin + "\" RETURN m LIMIT 50");
        while (res.hasNext()) {
            System.out.println(res.next().get("m").keys());
        }
    }

    private void findCommonMoviesForActors(String actorOne, String actorTwo) {
        String query = "MATCH (a1:Actor)-[:ACTED_IN]->(m:Movie)<-[:ACTED_IN]-(a2:Actor) WHERE a1.name CONTAINS \"" +
                actorOne + "\" AND a2.name CONTAINS \"" + actorTwo + "\" RETURN m";

        StatementResult res = session.run(query);
        while (res.hasNext()) {
            System.out.println(res.next().get("m").keys());
        }
    }

    private void findMovieRecommendationForUser(final String userLogin) {
        String query = "MATCH (u1:User)-[rate:RATED]->(:Movie)<-[:RATED]-(u2:User)-[:RATED]->(another:Movie) " +
                "WHERE u1.name CONTAINS \"" + userLogin +
                "\" WITH another, u1, u2, count(rate) as Times " +
                "WHERE Times > 3 " +
                "RETURN u1, u2, another " +
                "LIMIT 50";

        StatementResult res = session.run(query);
        while (res.hasNext()) {
            System.out.println(res.next().get("u1").keys());
        }
    }

}
