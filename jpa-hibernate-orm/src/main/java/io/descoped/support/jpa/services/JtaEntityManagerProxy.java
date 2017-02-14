package io.descoped.support.jpa.services;

import io.descoped.container.exception.DescopedServerException;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JtaEntityManagerProxy implements java.lang.reflect.InvocationHandler {

    private EntityManager em;

    private JtaEntityManagerProxy(EntityManager em) {
        this.em = em;
    }

    public static EntityManager newInstance(EntityManager em) {
        return (EntityManager) Proxy.newProxyInstance(em.getClass().getClassLoader(), new Class<?>[]{EntityManager.class}, new JtaEntityManagerProxy(em));
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result;
        try {
            if (!em.isJoinedToTransaction()) {
                em.joinTransaction();
            }
            result = m.invoke(em, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new DescopedServerException("Error joining transaction: " + e.getMessage(), e);
        }
        return result;
    }

}
