package io.descoped.support.jta.test;

import io.descoped.container.module.DescopedContainer;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import io.descoped.support.jta.JtaPrimitive;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 06/02/2017.
 */
@RunWith(JUnit4.class)
public class JtaPrimitiveTest {

    private static final Logger log = LoggerFactory.getLogger(JtaPrimitiveTest.class);
    private static DescopedContainer container;

    @BeforeClass
    public static void before() throws Exception {
        InstanceFactory<DescopedPrimitive> factory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        container = new DescopedContainer<>(factory);
        container.start();
    }

    @AfterClass
    public static void after() throws Exception {
        container.stop();
        container = null;
    }

    @Before
    public void setUp() throws Exception {
//        BeanProvider.injectFields(this);
    }

//    @Inject
//    UserTransaction utx;

    @Test
    public void testTxManager() throws Exception {
        TransactionManager transactionManager = JtaPrimitive.getTransaction();
        assertNotNull(transactionManager);
    }

    @Test
    public void testTxSyncManager() throws Exception {
        TransactionSynchronizationRegistry transactionManager = JtaPrimitive.TransactionSynchronizationRegistry();
        assertNotNull(transactionManager);
    }

    @Test
    public void testUserTransactionLookup() throws Exception {
        InitialContext ic = new InitialContext();
        UserTransaction utx = (UserTransaction) ic.lookup("java:/UserTransaction");
        assertNotNull(utx);
    }

    @Test
    public void testUserTransactionProducer() throws Exception {
//        assertNotNull(utx);
    }
}
