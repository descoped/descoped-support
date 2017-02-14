package io.descoped.support.jpa.test;

import io.descoped.support.jpa.test.entity.Person;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 * Created by oranheim on 09/02/2017.
 */
@Ignore
@RunWith(DescopedTestRunner.class)
public class JpaTest {

    private static final Logger log = LoggerFactory.getLogger(JpaTest.class);

    @Inject
    UserTransaction utx;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void testMe() throws Exception {
        log.trace("EMF: {}", emf);
        EntityManager em = emf.createEntityManager();

        Person p = new Person();
        p.setId("1");
        p.setFirstname("Ove");
        p.setLastname("Ranheim");

        em.joinTransaction();
        em.persist(p);
    }

}
