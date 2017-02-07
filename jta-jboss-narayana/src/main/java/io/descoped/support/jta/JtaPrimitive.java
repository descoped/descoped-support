package io.descoped.support.jta;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.jta.common.jtaPropertyManager;
import com.arjuna.ats.jta.utils.JNDIManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;

import javax.annotation.Priority;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

/**
 * Created by oranheim on 06/02/2017.
 */
@Priority(75)
@PrimitiveModule
public class JtaPrimitive implements DescopedPrimitive {

    private static final String TRANSACTION_MANAGER_JNDI = "java:/jboss/TransactionManager";
    private static final String REGISTRY_JNDI = "java:/jboss/TransactionSynchronizationRegistry";
    private boolean running = false;

    public static TransactionManager getTransaction() throws NamingException {
        return (TransactionManager) new InitialContext().lookup(TRANSACTION_MANAGER_JNDI);
    }

    public static TransactionSynchronizationRegistry TransactionSynchronizationRegistry() throws NamingException {
        TransactionSynchronizationRegistry transactionManager = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:/jboss/TransactionSynchronizationRegistry");
        return transactionManager;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {
        if (!running) {
            // Bind JTA implementation with default names
            try {
                jtaPropertyManager.getJTAEnvironmentBean().setTransactionManagerJNDIContext(TRANSACTION_MANAGER_JNDI);
                jtaPropertyManager.getJTAEnvironmentBean().setTransactionSynchronizationRegistryJNDIContext(REGISTRY_JNDI);

                JNDIManager.bindJTAImplementation();

                BeanPopulator.getDefaultInstance(ObjectStoreEnvironmentBean.class).setObjectStoreDir("target/tx-object-store");
                BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore").setObjectStoreDir("target/tx-object-store");
                BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "stateStore").setObjectStoreDir("target/tx-object-store");

            } catch (NamingException e) {
                throw new DescopedServerException(e);
            }
        }
    }

    @Override
    public void stop() {
        if (running) {
            // do nothing
            try {
                JNDIManager.unbindJTATransactionSynchronizationRegistryImplementation();
                JNDIManager.unbindJTATransactionManagerImplementation();
            } catch (NamingException e) {
                throw new DescopedServerException(e);
            }
        }

    }

    @Override
    public void destroy() {

    }

}
