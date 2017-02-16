package io.descoped.support.jta.test;

import io.descoped.support.jta.primitive.JtaPrimitive;
import io.descoped.support.jta.status.TransactionStatus;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 06/02/2017.
 */
@RunWith(DescopedTestRunner.class)
public class JtaPrimitiveTest {

    private static final Logger log = LoggerFactory.getLogger(JtaPrimitiveTest.class);

    @Inject
    UserTransaction utx;

    @Test
    public void testTxSyncManager() throws Exception {
        TransactionSynchronizationRegistry transactionManager = JtaPrimitive.getTransactionSynchronizationRegistry();
        assertNotNull(transactionManager);
    }

    @Test
    public void testTxManager() throws Exception {
        TransactionManager transactionManager = JtaPrimitive.getTransaction();
        assertNotNull(transactionManager);
    }

    @Test
    public void testUserTransactionLookup() throws Exception {
        UserTransaction utx = JtaPrimitive.getUserTransaction();
        assertNotNull(utx);
    }

    @Test(expected = IllegalStateException.class)
    public void testUserTransactionProducer() throws Exception {
        assertNotNull(utx);
        log.trace("utx status - before utx.begin: {}", TransactionStatus.valueOf(utx.getStatus()));
        utx.begin();
        log.trace("utx status - after utx.begin: {}", TransactionStatus.valueOf(utx.getStatus()));
        utx.commit();
        log.trace("utx status - after utx.commit: {}", TransactionStatus.valueOf(utx.getStatus()));
        utx.rollback();
    }
}
