package io.descoped.support.jta.primitive;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.internal.arjuna.objectstore.NullActionStore;
import com.arjuna.ats.jta.common.jtaPropertyManager;
import com.arjuna.ats.jta.utils.JNDIManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;

import javax.annotation.Priority;
import javax.enterprise.inject.Vetoed;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

/**
 * Created by oranheim on 06/02/2017.
 */
@Priority(60)
@PrimitiveModule
@Vetoed
public class JtaPrimitive implements DescopedPrimitive {

    private static final String TRANSACTION_MANAGER_JNDI = "java:/jboss/TransactionManager";
    private static final String REGISTRY_JNDI = "java:/jboss/TransactionSynchronizationRegistry";
    private boolean nostore = false;
    private boolean running;

    public static TransactionManager getTransaction() throws NamingException {
        return (TransactionManager) new InitialContext().lookup(TRANSACTION_MANAGER_JNDI);
    }

    public static TransactionSynchronizationRegistry TransactionSynchronizationRegistry() throws NamingException {
        TransactionSynchronizationRegistry transactionManager = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:/jboss/TransactionSynchronizationRegistry");
        return transactionManager;
    }

    public boolean isNostore() {
        return nostore;
    }

    public void setNostore(boolean nostore) {
        this.nostore = nostore;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {
//        if (!isRunning()) {
        if (!running) {
            // Bind JTA implementation with default names
            try {
                jtaPropertyManager.getJTAEnvironmentBean().setTransactionManagerJNDIContext(TRANSACTION_MANAGER_JNDI);
                jtaPropertyManager.getJTAEnvironmentBean().setTransactionSynchronizationRegistryJNDIContext(REGISTRY_JNDI);

                JNDIManager.bindJTAImplementation();

                if (nostore) {
                    BeanPopulator.getDefaultInstance(ObjectStoreEnvironmentBean.class).setObjectStoreType(NullActionStore.class.getName());

                } else {
                    //BeanPopulator.getDefaultInstance(ObjectStoreEnvironmentBean.class).setObjectStoreType(ShadowNoFileLockStore.class.getName()); // default
                    BeanPopulator.getDefaultInstance(ObjectStoreEnvironmentBean.class).setObjectStoreDir("target/tx-object-store");
                    BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore").setObjectStoreDir("target/tx-object-store");
                    BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "stateStore").setObjectStoreDir("target/tx-object-store");
                }

                running = true;

            } catch (NamingException e) {
                throw new DescopedServerException(e);
            }
        }
    }

    @Override
    public void stop() {
        if (running) {
            try {
                // reverse of: JNDIManager.bindJTAImplementation();
                JNDIManager.unbindJTATransactionSynchronizationRegistryImplementation();
                //JNDIManager.unbindJTAUserTransactionImplementation(); // todo: should be unbinded on shutdown
                JNDIManager.unbindJTATransactionManagerImplementation();

                running = false;
            } catch (NamingException e) {
                throw new DescopedServerException(e);
            }
        }

    }

    @Override
    public void destroy() {

    }

}
