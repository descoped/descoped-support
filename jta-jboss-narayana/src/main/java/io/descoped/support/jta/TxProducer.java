package io.descoped.support.jta;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by oranheim on 06/02/2017.
 */
@ApplicationScoped
public class TxProducer {

//    @Produces
//    @Dependent
//    TransactionManager produceTransactionManager() throws NamingException {
//        return JtaPrimitive.getTransaction();
//    }

//    @Produces
//    @Dependent
//    Transaction produceTransaction() throws NamingException, SystemException {
//        return JtaPrimitive.getTransaction().getTransaction();
//    }

//    @Produces
//    @Dependent
//    UserTransaction produceUserTransaction() throws NamingException, SystemException {
//        InitialContext ic = new InitialContext();
//        UserTransaction utx = (UserTransaction) ic.lookup("java:/UserTransaction");
//        return utx;
//    }
}
