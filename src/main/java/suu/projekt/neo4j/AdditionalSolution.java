package suu.projekt.neo4j;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class AdditionalSolution extends AbstractSolution { //TODO rewrite to new API

    AdditionalSolution(Session session) {
        super(session);
    }

    public void databaseStatistics() {
        System.out.println(session.run("CALL db.labels()"));
        System.out.println(session.run("CALL db.relationshipTypes()"));
    }

    public void runAllTests() {
        System.out.println(newActorAndRelation("Emma Watson", "Star Wars", "ACTS_IN"));
        System.out.println(actorSetProperties("Emma Watson", "UK", "02-03-1996"));
        System.out.println(actorAtLeastSixMovies());
        System.out.println(averageForActorsAtLeastSevenMovies());
        System.out.println(actorAtLeastFiveMoviesDirectedOne());
        System.out.println(usersFriendsRatedThreeStars("emileifrem"));
    }


    private StatementResult newActorAndRelation(String actorName, String movieTitle, String relation) {
        String queryActor = "CREATE (n: Actor {name: '" + actorName + "'}) RETURN n";
        String queryMovie = "CREATE (n: Movie {title: '" + movieTitle + "'}) RETURN n";
        String queryRelation = "MATCH (a: Actor), (m: Movie) WHERE a.name = '" + actorName + "' and m.title =" +
                "'" + movieTitle + "' CREATE (a)-[r: " + relation + "]->(m) RETURN r";

        StatementResult result1 = session.run(queryActor);
        StatementResult result2 = session.run(queryMovie);
        StatementResult result3 = session.run(queryRelation);

        return result1; // + result2 + result3;
    }

    private StatementResult actorSetProperties(String actorName, String birthplace, String birthdate) {
        String query = "MATCH (a: Actor) where a.name = '" + actorName +
                "' SET a.birthplace='" + birthplace + "', a.birthday='" + birthdate + "'";

        return session.run(query);
    }

    private StatementResult actorAtLeastSixMovies() {

        String query = "MATCH (actor: Actor)-[:ACTS_IN]->(movie: Movie) " +
                "WITH actor, collect(movie) as movies WHERE length(movies) > 5" +
                " RETURN actor.name, length(movies)";

        return session.run(query);
    }

    private StatementResult averageForActorsAtLeastSevenMovies() {

        String query = "MATCH (actor: Actor)-[:ACTS_IN]->(movie: Movie) " +
                "WITH actor, collect(movie) as movies WHERE length(movies) > 6" +
                " RETURN avg(length(movies)) as average";

        return session.run(query);
    }

    private StatementResult actorAtLeastFiveMoviesDirectedOne() {

        String query = "MATCH (actor:Actor) -[:ACTS_IN]-> (movie:Movie) " +
                "WITH actor, count(movie) as movies " +
                "WHERE movies > 4 " +
                "WITH actor, movies " +
                "MATCH (actor:Director) -[:DIRECTED]-> (movie:Movie) " +
                "WITH actor, movies, count(movie.title) as directed, collect(movie.title) as titles " +
                "WHERE directed > 0 " +
                "Return actor.name, movies, directed, titles " +
                "ORDER BY movies DESC ";

        return session.run(query);
    }

    private StatementResult usersFriendsRatedThreeStars(String username) {

        String query = "MATCH (user:Person{login: '" + username + "'}) -[:FRIEND]->(friend)-[rate:RATED]->(movie: Movie) WHERE rate.stars > 2" +
                " RETURN friend.name, movie.title, rate.stars";

        return session.run(query);
    }

}
