package io.descoped.support.jta.test;

import io.descoped.logger.logback.handler.LogbackBridgeHandler;
import org.jboss.weld.environment.se.Weld;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by oranheim on 09/02/2017.
 */
@Ignore
@RunWith(JUnit4.class)
public class CdiTest {

    private static Weld weld;

    @BeforeClass
    public static void before() throws Exception {
        LogbackBridgeHandler.installJavaUtilLoggerBridgeHandler();
//        cdi = CdiContainerLoader.getCdiContainer();
//        cdi.boot();
        weld = new Weld();
        weld.initialize();
    }

    @AfterClass
    public static void after() throws Exception {
//        cdi.shutdown();
        weld.shutdown();
        weld = null;
    }

    @Test
    public void testMe() throws Exception {
    }
}
