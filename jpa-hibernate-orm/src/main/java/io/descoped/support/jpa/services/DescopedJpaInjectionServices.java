package io.descoped.support.jpa.services;

import io.descoped.container.exception.DescopedServerException;
import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 * Created by oranheim on 11/02/2017.
 */
public class DescopedJpaInjectionServices implements JpaInjectionServices {

    private static final Logger log = LoggerFactory.getLogger(DescopedJpaInjectionServices.class);

    public DescopedJpaInjectionServices() {
//        log.trace("------------------------------------------------ create()");
    }

    @Override
    public ResourceReferenceFactory<EntityManager> registerPersistenceContextInjectionPoint(InjectionPoint injectionPoint) {
        if (injectionPoint.getAnnotated().isAnnotationPresent(PersistenceContext.class)) {
            PersistenceContext persistenceContextAnno = injectionPoint.getAnnotated().getAnnotation(PersistenceContext.class);
            EntityManagerResourceReferenceFactory factory = new EntityManagerResourceReferenceFactory(persistenceContextAnno);
            return factory;
        }
        throw new DescopedServerException("Unable to handle creation of PersistenceContext!");
    }

    @Override
        public ResourceReferenceFactory<EntityManagerFactory> registerPersistenceUnitInjectionPoint(InjectionPoint injectionPoint) {
        if (injectionPoint.getAnnotated().isAnnotationPresent(PersistenceUnit.class)) {
            PersistenceUnit persistenceUnitAnno = injectionPoint.getAnnotated().getAnnotation(PersistenceUnit.class);
            EntityManagerFactoryResourceReferenceFactory factory = new EntityManagerFactoryResourceReferenceFactory(persistenceUnitAnno);
            return factory;
        }
        throw new DescopedServerException("Unable to handle creation of PersistenceUnit!");
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
