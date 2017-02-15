package io.descoped.support.jpa.test;

import io.descoped.support.jpa.test.entity.EntityManagerBean;
import io.descoped.support.jpa.test.entity.Person;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.hibernate.internal.SessionImpl;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by oranheim on 09/02/2017.
 */
@RunWith(DescopedTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaTest {

    private static final Logger log = LoggerFactory.getLogger(JpaTest.class);

    @Inject
    UserTransaction utx;

    @Inject
    EntityManagerBean entityManagerBean;

    @Resource(mappedName = "em3")
    DataSource dataSource;

    @PersistenceUnit(unitName = "PU1")
    private EntityManagerFactory emf;

    @BeforeClass
    public static void before() throws Exception {
        CdiContainerLoader.getCdiContainer().getContextControl().startContext(RequestScoped.class);
    }

    private void countPersonsFixture(EntityManager em) throws SystemException, NotSupportedException, SQLException {
        log.trace("EMF: {} -- EM: {} -- DS: {}", emf.getClass(), em.getClass(), dataSource.getClass());

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
    public void test_0_PersistenceContext() throws Exception {
        EntityManager em = entityManagerBean.em();
        assertEquals(SessionImpl.class, em.getClass());

        Person p1 = new Person("John", "Dee");
        Person p2 = new Person("Jane", "Roe");

        utx.begin();
        try {
            em.joinTransaction();
            em.persist(p1);
            em.persist(p2);
            utx.commit();

        } catch (Exception e) {
            utx.rollback();
        }

        countPersonsFixture(em);
    }

    @Test
    public void test_1_EntityManagerProxy() throws Exception {
        EntityManager em = entityManagerBean.emProxy().get();
        assertEquals(EntityManager.class, em.getClass().getInterfaces()[0]); // emit proxy

        Person p3 = new Person("John", "Roe");
        Person p4 = new Person("Jane", "Doe");

        utx.begin();
        try {
            em.persist(p3);
            em.persist(p4);
            utx.commit();

        } catch (Exception e) {
            utx.rollback();
        }

        utx.begin();
        countPersonsFixture(em);
        utx.commit();
    }

    @Test(expected = IllegalStateException.class)
    public void test_2_PersistenceContext() throws Exception {
        test_0_PersistenceContext();
    }

    @Test(expected = IllegalStateException.class)
    public void test_3_EntityManagerProxy() throws Exception {
        test_1_EntityManagerProxy();
    }
}
