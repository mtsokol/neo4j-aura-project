package suu.projekt.neo4j;

import org.neo4j.driver.v1.*;

public class Main {

    public static void main(String... args) {

        String url = "NEO4J_URL";
        String user = "NEO4J_USER";
        String pass = "NEO4J_PWD";

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