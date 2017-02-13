package io.descoped.support.jta;

import io.descoped.container.exception.DescopedServerException;
import org.jboss.weld.transaction.spi.TransactionServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Created by oranheim on 06/02/2017.
 * <p>
 * https://github.com/arquillian/arquillian-extension-transaction/tree/master/impl-jta/src/main/java/org/jboss/arquillian/transaction/jta
 * http://www.laliluna.com/articles/2011/01/12/jboss-weld-jpa-hibernate.html
 */
@Priority(75)
public class DescopedTransactionServices implements TransactionServices {

    private static final Logger log = LoggerFactory.getLogger(DescopedTransactionServices.class);
    private static int count = 0;

    public DescopedTransactionServices() {
        log.trace("Create DescopedTransactionServices..");
//        try {
//            if (count == 1)
//                throw new RuntimeException("XXX");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        count++;
    }


    @Override
    public void registerSynchronization(Synchronization synchronization) {
        log.trace("-------> registerSynchronization({})", synchronization);
        try {
            JtaPrimitive.TransactionSynchronizationRegistry().registerInterposedSynchronization(synchronization);
        } catch (NamingException e) {
            throw new DescopedServerException(e);
        }
    }

    @Override
    public boolean isTransactionActive() {
        log.trace("-------> isTransactionActive");
        try {
            return JtaPrimitive.getTransaction().getStatus() == Status.STATUS_ACTIVE;
        } catch (SystemException | NamingException e) {
            throw new DescopedServerException(e);
        }
    }

    @Override
    public UserTransaction getUserTransaction() {
        log.trace("-------> getUserTransaction");
        try {
            InitialContext ic = new InitialContext();
            UserTransaction utx = (UserTransaction) ic.lookup("java:/UserTransaction");
            return utx;
        } catch (NamingException e) {
            throw new DescopedServerException(e);
        }
    }

    @Override
    public void cleanup() {
    }

}
