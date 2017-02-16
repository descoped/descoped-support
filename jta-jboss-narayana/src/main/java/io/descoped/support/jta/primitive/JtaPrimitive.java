package io.descoped.support.jta.primitive;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.internal.arjuna.objectstore.NullActionStore;
import com.arjuna.ats.jta.common.jtaPropertyManager;
import com.arjuna.ats.jta.utils.JNDIManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import org.jnp.interfaces.NamingContext;

import javax.annotation.Priority;
import javax.enterprise.inject.Vetoed;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

/**
 * Created by oranheim on 06/02/2017.
 */
@Priority(60)
@PrimitiveModule
@Vetoed
public class JtaPrimitive implements DescopedPrimitive {

    private static final String TRANSACTION_SYNCRONIZATION_REGISTRY_JNDI_CONTEXT = "java:/TransactionSynchronizationRegistry";
    private static final String TRANSACTION_MANAGER_JNDI_CONTEXT = "java:/TransactionManager";
    private static final String USER_TRANSACTION_JNDI_CONTEXT = "java:/UserTransaction";

    private static final String TRANSACTION_SYNCRONIZATION_REGISTRY_JNDI_COMP_CONTEXT = "java:comp/TransactionSynchronizationRegistry";
    private static final String TRANSACTION_MANAGER_JNDI_COMP_CONTEXT = "java:comp/TransactionManager";
    private static final String USER_TRANSACTION_JNDI_COMP_CONTEXT = "java:comp/UserTransaction";

    private boolean nostore = false;
    private boolean running;

    public static TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() throws NamingException {
        TransactionSynchronizationRegistry transactionManager = (TransactionSynchronizationRegistry) new InitialContext().lookup(TRANSACTION_SYNCRONIZATION_REGISTRY_JNDI_CONTEXT);
        return transactionManager;
    }

    public static TransactionManager getTransaction() throws NamingException {
        return (TransactionManager) new InitialContext().lookup(TRANSACTION_MANAGER_JNDI_CONTEXT);
    }

    public static UserTransaction getUserTransaction() throws NamingException {
        return (UserTransaction) new InitialContext().lookup(USER_TRANSACTION_JNDI_CONTEXT);
    }


    private void bindJTAImplementation(String jndiName, String jtaImplementationClass) throws NamingException {
        InitialContext ic = new InitialContext();
        Reference ref = new Reference(jtaImplementationClass, jtaImplementationClass, null);
        if (jndiName.startsWith("java:comp")) {
            String jndiNameComp = jndiName.substring("java:comp".length()+1);
            NamingContext nc = (NamingContext) ic.lookup("java:comp");
            nc.rebind(jndiNameComp, ref);
        } else {
            ic.rebind(jndiName, ref);
        }
    }

    private void unbindJTAImplementation(String jndiName) throws NamingException {
        InitialContext ic = new InitialContext();
        if (jndiName.startsWith("java:comp")) {
            String jndiNameComp = jndiName.substring("java:comp".length()+1);
            NamingContext nc = (NamingContext) ic.lookup("java:comp");
            nc.unbind(jndiNameComp);
        } else {
            ic.unbind(jndiName);
        }
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
                jtaPropertyManager.getJTAEnvironmentBean().setTransactionManagerJNDIContext(TRANSACTION_MANAGER_JNDI_CONTEXT);
                jtaPropertyManager.getJTAEnvironmentBean().setUserTransactionJNDIContext(USER_TRANSACTION_JNDI_CONTEXT);
                jtaPropertyManager.getJTAEnvironmentBean().setTransactionSynchronizationRegistryJNDIContext(TRANSACTION_SYNCRONIZATION_REGISTRY_JNDI_CONTEXT);

                JNDIManager.bindJTAImplementation();

                bindJTAImplementation(TRANSACTION_MANAGER_JNDI_COMP_CONTEXT, jtaPropertyManager.getJTAEnvironmentBean().getTransactionManagerClassName());
                bindJTAImplementation(USER_TRANSACTION_JNDI_COMP_CONTEXT, jtaPropertyManager.getJTAEnvironmentBean().getUserTransactionClassName());
                bindJTAImplementation(TRANSACTION_SYNCRONIZATION_REGISTRY_JNDI_COMP_CONTEXT, jtaPropertyManager.getJTAEnvironmentBean().getTransactionSynchronizationRegistryClassName());

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
                unbindJTAImplementation(TRANSACTION_SYNCRONIZATION_REGISTRY_JNDI_COMP_CONTEXT);
                unbindJTAImplementation(TRANSACTION_MANAGER_JNDI_COMP_CONTEXT);
                unbindJTAImplementation(USER_TRANSACTION_JNDI_COMP_CONTEXT);

                unbindJTAImplementation(TRANSACTION_SYNCRONIZATION_REGISTRY_JNDI_CONTEXT);
                unbindJTAImplementation(TRANSACTION_MANAGER_JNDI_CONTEXT);
                unbindJTAImplementation(USER_TRANSACTION_JNDI_CONTEXT);

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
