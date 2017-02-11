package io.descoped.support.jpa.test;

import io.descoped.support.jpa.test.entity.Person;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 * Created by oranheim on 09/02/2017.
 */
@RunWith(DescopedTestRunner.class)
public class JpaTest {

    private static final Logger log = LoggerFactory.getLogger(JpaTest.class);

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Before
    public void before() {
        BootstrapServiceRegistryBuilder bootstrapRegistryBuilder =
                new BootstrapServiceRegistryBuilder();
        // add a custom ClassLoader
        bootstrapRegistryBuilder.applyClassLoader(JpaTest.class.getClassLoader());

        // manually add an Integrator
//        bootstrapRegistryBuilder.applyIntegrator(customIntegrator);

        BootstrapServiceRegistry bootstrapRegistry = bootstrapRegistryBuilder.build();

        emf = Persistence.createEntityManagerFactory( "CRM" );
    }

    @Test
    public void testMe() throws Exception {
        log.trace("EMF: {}", emf);
        EntityManager em = emf.createEntityManager();

        Person p = new Person();
        p.setId("1");
        p.setFirstname("Ove");
        p.setLastname("Ranheim");

        em.persist(p);

    }

}
