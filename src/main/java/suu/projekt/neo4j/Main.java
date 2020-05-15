package suu.projekt.neo4j;

import org.neo4j.driver.v1.*;

public class Main {

    public static void main(String... args) {

        String url = System.getenv("NEO4J_URL");
        String user = System.getenv("NEO4J_USER");
        String pass = System.getenv("NEO4J_PASS");

        Config noSSL = Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
        Driver driver = GraphDatabase.driver(url, AuthTokens.basic(user, pass), noSSL);

        try (Session session = driver.session()) {

            Solution solution = new Solution(session);
            solution.databaseStatistics();
            solution.runAllTests();

            AdditionalSolution additionalSolution = new AdditionalSolution(session);
            additionalSolution.databaseStatistics();
            additionalSolution.runAllTests();

        }
    }
}