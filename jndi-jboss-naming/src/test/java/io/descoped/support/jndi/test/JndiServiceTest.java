package io.descoped.support.jndi.test;

import io.descoped.container.module.DescopedContainer;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 06/02/2017.
 */
@RunWith(JUnit4.class)
public class JndiServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JndiServiceTest.class);
    private static DescopedContainer container;

    @BeforeClass
    public static void setUp() throws Exception {
        InstanceFactory<DescopedPrimitive> factory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        container = new DescopedContainer<>(factory);
        container.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        container.stop();
        container = null;
    }

    public static class Foo implements Serializable {
        String bar = "Baz";
    }

    @Test
    public void test_0_CreateJndiBinding() throws Exception {
        InitialContext ic = new InitialContext();
        ic.bind("java:/foo", new Foo());
    }

    @Test
    public void test_1_LookupJndiBinding() throws Exception {
        InitialContext ic = new InitialContext();
        Foo foo = (Foo) ic.lookup("java:/foo");
        assertNotNull(foo);
        assertEquals("Baz", foo.bar);
    }
}
