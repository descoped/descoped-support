package io.descoped.support.jpa.services;

import io.descoped.container.exception.DescopedServerException;
import org.jboss.weld.injection.spi.ResourceReference;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by oranheim on 14/02/2017.
 */
public class EntityManagerResourceReferenceFactory implements ResourceReferenceFactory<EntityManager> {

    private final String unitName;
    private final PersistenceContext persistenceContextAnnotation;

    public EntityManagerResourceReferenceFactory(PersistenceContext persistenceContextAnnotation) {
        this.unitName = persistenceContextAnnotation.unitName();
        this.persistenceContextAnnotation = persistenceContextAnnotation;
    }

    private boolean isNotNull(String value) {
        return value != null && !"".equals(value);
    }

    @Override
    public ResourceReference<EntityManager> createResource() {
        if (isNotNull(unitName)) {
            EntityManagerResourceReference factory = new EntityManagerResourceReference(persistenceContextAnnotation);
            return factory;
        }
        throw new DescopedServerException("Unable to create resource reference for EntityManager: " + unitName);

    }
}
