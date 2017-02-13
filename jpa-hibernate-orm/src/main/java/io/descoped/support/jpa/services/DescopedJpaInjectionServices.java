package io.descoped.support.jpa.services;

import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by oranheim on 11/02/2017.
 */
public class DescopedJpaInjectionServices implements JpaInjectionServices {

    private static final Logger log = LoggerFactory.getLogger(DescopedJpaInjectionServices.class);

    public DescopedJpaInjectionServices() {
        log.trace("------------------------------------------------ create()");
    }

    @Override
    public ResourceReferenceFactory<EntityManager> registerPersistenceContextInjectionPoint(InjectionPoint injectionPoint) {
        log.trace("------------------------------------------------ registerPersistenceContextInjectionPoint: {}", injectionPoint);
        return null;
    }

    @Override
    public ResourceReferenceFactory<EntityManagerFactory> registerPersistenceUnitInjectionPoint(InjectionPoint injectionPoint) {
        log.trace("------------------------------------------------ registerPersistenceUnitInjectionPoint: {}", injectionPoint);
        return null;
    }

    @Override
    public EntityManager resolvePersistenceContext(InjectionPoint injectionPoint) {
        throw new UnsupportedOperationException("resolvePersistenceContext was deprecated in Weld 2.x!");
    }

    @Override
    public EntityManagerFactory resolvePersistenceUnit(InjectionPoint injectionPoint) {
        throw new UnsupportedOperationException("resolvePersistenceUnit was deprecated in Weld 2.x!");
    }

    @Override
    public void cleanup() {
//        log.trace("------------------------------------------------ cleanup()");
    }

}
