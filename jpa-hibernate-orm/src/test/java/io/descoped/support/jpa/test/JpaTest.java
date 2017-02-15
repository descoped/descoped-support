package io.descoped.support.jpa.test;

import io.descoped.support.jpa.test.entity.EmBean;
import io.descoped.support.jpa.test.entity.Person;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
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
import java.util.ArrayList;
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


//    @Test
    public void testDiscoveryEntity() throws Exception {
        List<String> classNamesList = new ArrayList<>();


        ScanResult x = new FastClasspathScanner(new String[]
                {"io.descoped"})  // Whitelisted package prefixes
                .matchClassesWithAnnotation(Entity.class, c -> {
                    log.trace("Found class: {}", c);
                    classNamesList.add(c.getName());
                })
                .scan();
//        x.getClassNameToClassInfo().values();
//        System.out.println("------------------------------------------------------: " + classNamesList.size());
        classNamesList.forEach(c -> {
            System.out.println("------------------------------------------------------: " + c);
        });
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
