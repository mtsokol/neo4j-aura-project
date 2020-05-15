package suu.projekt.neo4j;

import org.neo4j.driver.v1.Session;

abstract public class AbstractSolution {

    protected final Session session;

    AbstractSolution(Session session) {
        this.session = session;
    }

}
