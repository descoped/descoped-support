package io.descoped.support.jta.test;

import io.descoped.support.jta.status.TransactionStatus;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import org.jboss.tm.TransactionManagerLocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 16/02/2017.
 */
@RunWith(DescopedTestRunner.class)
public class TxTest {

    private static final Logger log = LoggerFactory.getLogger(TxTest.class);

    @Inject
    UserTransaction utx;

    @Test
    public void testTransactionManager() throws Exception {
        InitialContext ic = new InitialContext();
        TransactionManager tm = (TransactionManager) ic.lookup("java:/TransactionManager");
        assertNotNull(tm);

        log.trace("--tx1 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.NO_TRANSACTION, TransactionStatus.valueOf(tm.getStatus()));

        tm.begin();
        log.trace("--tx2 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.ACTIVE, TransactionStatus.valueOf(tm.getStatus()));

        tm.commit();
        log.trace("--tx3 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.NO_TRANSACTION, TransactionStatus.valueOf(tm.getStatus()));
    }

    @Test
    public void testTransactionLocator() throws Exception {
        TransactionManager tm = TransactionManagerLocator.getInstance().locate(true);
        assertNotNull(tm);

        log.trace("--tx1 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm.toString());
        assertEquals(TransactionStatus.NO_TRANSACTION, TransactionStatus.valueOf(tm.getStatus()));

        tm.begin();
        log.trace("--tx2 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.ACTIVE, TransactionStatus.valueOf(tm.getStatus()));

        tm.commit();
        log.trace("--tx3 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.NO_TRANSACTION, TransactionStatus.valueOf(tm.getStatus()));
    }

    @Test
    public void testTransactionLocatorWithUserTransaction() throws Exception {
        TransactionManager tm = TransactionManagerLocator.getInstance().getTransactionManager();
        assertNotNull(tm);

        log.trace("--tx1 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.NO_TRANSACTION, TransactionStatus.valueOf(tm.getStatus()));

        utx.begin();
        log.trace("--tx2 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.ACTIVE, TransactionStatus.valueOf(tm.getStatus()));

        utx.commit();
        log.trace("--tx3 status: {} => {}", TransactionStatus.valueOf(tm.getStatus()), tm);
        assertEquals(TransactionStatus.NO_TRANSACTION, TransactionStatus.valueOf(tm.getStatus()));
    }

}
