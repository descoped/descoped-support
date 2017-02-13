package io.descoped.support.jpa.test;

import io.descoped.support.jpa.test.entity.Person;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by oranheim on 09/02/2017.
 */
@RunWith(DescopedTestRunner.class)
public class JpaTest {

//    @PersistenceUnit
    private EntityManagerFactory emf;

//    @PersistenceContext
    private EntityManager em;

    @Resource(mappedName = "em1")
//    @Resource(lookup = "java:/jdbc/ProdDS")
    DataSource testDataSource;

//    @Inject
//    private EmBean emBean;

    private static final Logger log = LoggerFactory.getLogger(JpaTest.class);

//    @Before
    public void before() {
        BootstrapServiceRegistryBuilder bootstrapRegistryBuilder =
                new BootstrapServiceRegistryBuilder();
        // add a custom ClassLoader
        bootstrapRegistryBuilder.applyClassLoader(JpaTest.class.getClassLoader());

        // manually add an Integrator
//        bootstrapRegistryBuilder.applyIntegrator(customIntegrator);

        BootstrapServiceRegistry bootstrapRegistry = bootstrapRegistryBuilder.build();

//        emf = Persistence.createEntityManagerFactory( "CRM" );
    }

//    @Test
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

    @Test
    public void testDataSource() throws Exception {
        log.trace("----> testDataSource: {}", testDataSource);
    }

}
