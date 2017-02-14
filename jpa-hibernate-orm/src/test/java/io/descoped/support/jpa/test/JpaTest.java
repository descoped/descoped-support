package io.descoped.support.jpa.test;

import io.descoped.support.jpa.test.entity.EmBean;
import io.descoped.support.jpa.test.entity.Person;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by oranheim on 09/02/2017.
 */
@RunWith(DescopedTestRunner.class)
public class JpaTest {

    private static final Logger log = LoggerFactory.getLogger(JpaTest.class);

    @Inject
    UserTransaction utx;

    @Inject
    EmBean emBean;

    @Resource(mappedName = "em3")
    DataSource dataSource;

    @PersistenceUnit(unitName = "PU1")
    private EntityManagerFactory emf;

//    @PersistenceContext(unitName = "PU1")
//    @Inject
    private EntityManager em;

    @BeforeClass
    public static void before() throws Exception {
        CdiContainerLoader.getCdiContainer().getContextControl().startContext(RequestScoped.class);
    }

    @Test
    public void testDiscoveryEntity() throws Exception {

        new FastClasspathScanner(new String[]
                {"io.descoped"})  // Whitelisted package prefixes
//                .matchSubclassesOf(Object.class,
//                        // c is a subclass of DBModel
//                        c -> System.out.println("Found subclass of Object: " + c.getName()))
//                .matchClassesImplementing(Runnable.class,
//                        // c is a class that implements Runnable
//                        c -> System.out.println("Found Runnable: " + c.getName()))
                .matchClassesWithAnnotation(Entity.class,
                        // c is a class annotated with @RestHandler
                        c -> System.out.println("Found Entity annotation on class: "
                                + c.getName()))
//                .matchFilenamePattern("^template/.*\\.html",
//                        // templatePath is a path on the classpath that matches the above pattern;
//                        // inputStream is a stream opened on the file or zipfile entry.
//                        // No need to close inputStream before exiting, it is closed by caller.
//                        (templatePath, inputStream) -> {
//                            try {
//                                String template = IOUtils.toString(inputStream, "UTF-8");
//                                System.out.println("Found template: " + absolutePath
//                                        + " (size " + template.length() + ")");
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        })

                .scan();  // Actually perform the scan
    }

    @Test
    public void testMe() throws Exception {
        em = emBean.em();
        log.trace("EMF: {} => {} <= {}", emf.getClass(), em.getClass(), dataSource);
//        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("1", "John", "Dee");
        Person p2 = new Person("2", "Jane", "Roe");

        utx.begin();
        try {
            em.joinTransaction();

            em.persist(p1);

            em.persist(p2);

//            if (true) throw new DescopedServerException("Something went wrong");

            utx.commit();


        } catch (Exception e) {
            utx.rollback();
        }

        Query query = em.createQuery("SELECT p FROM Person p");
        List result = query.getResultList();
        log.trace("Result: {}", result.size());

        Connection conn = dataSource.getConnection();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Person");
        log.trace("{}\t{}\t{}", rs.getMetaData().getColumnLabel(1), rs.getMetaData().getColumnLabel(2), rs.getMetaData().getColumnLabel(3));
        log.trace("--\t---------\t--------");
        while (rs.next()) {
            log.trace("{}\t{}\t\t{}", rs.getString(1), rs.getString(2), rs.getString(3));
        }
    }

    @Test
    public void testMe2() throws Exception {


    }
}
