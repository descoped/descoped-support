package io.descoped.support.jpa.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Created by oranheim on 11/02/2017.
 */
@ApplicationScoped
public class EntityManagerProducer {

    private static final Logger log = LoggerFactory.getLogger(EntityManagerProducer.class);

    @PersistenceUnit(unitName = "PU1")
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @Default
    @RequestScoped
    protected EntityManager createEntityManager() {
        log.trace("------------------------------------------> Produce: EntityManager");
        return entityManagerFactory.createEntityManager();
    }

    protected void closeEntityManager(@Disposes EntityManager entityManager) {
        log.trace("------------------------------------------> Close EM: {} => {}", entityManager.hashCode(), entityManager.isOpen());
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

}
