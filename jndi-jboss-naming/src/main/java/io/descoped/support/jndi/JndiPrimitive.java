package io.descoped.support.jndi;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import org.jnp.interfaces.NamingParser;
import org.jnp.server.NamingBeanImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.inject.Vetoed;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * Created by oranheim on 06/02/2017.
 */
@Priority(50)
@PrimitiveModule
@Vetoed
public class JndiPrimitive implements DescopedPrimitive {

    public static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
    public static final String JNP_INTERFACES = "org.jboss.naming:org.jnp.interfaces";
    public static final String COMP = "comp/env";
    public static final String JBOSS = "jboss";
    public static final String JDBC = "jdbc";
    private static final Logger log = LoggerFactory.getLogger(JndiPrimitive.class);
    private NamingBeanImpl namingBean;
    private Hashtable env;

    public NamingBeanImpl getNamingBean() {
        return namingBean;
    }

    @Override
    public void init() {
        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.URL_PKG_PREFIXES, JNP_INTERFACES);

        // skipping because thier set in 'jndi.properties'
//        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
//        System.setProperty(Context.URL_PKG_PREFIXES, JNP_INTERFACES);

        try {
            InitialContext initialContext = new InitialContext(env);
            Field aField = org.apache.deltaspike.core.impl.util.JndiUtils.class.getDeclaredField("initialContext");
            aField.setAccessible(true);
            aField.set(null, initialContext);
        } catch (NamingException | IllegalAccessException | NoSuchFieldException e) {
            throw new DescopedServerException(e);
        }

        namingBean = new NamingBeanImpl();
    }

    @Override
    public void start() {
        try {
            namingBean.start();
//            namingBean.getNamingInstance().createSubcontext(new NamingParser().parse(COMP));
            namingBean.getNamingInstance().createSubcontext(new NamingParser().parse(JBOSS));
            namingBean.getNamingInstance().createSubcontext(new NamingParser().parse(JDBC));

        } catch (Exception e) {
            throw new DescopedServerException(e);
        }
    }

    @Override
    public void stop() {
        namingBean.stop();
    }

    @Override
    public void destroy() {
        namingBean = null;
    }
}
