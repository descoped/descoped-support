package io.descoped.support.jpa.services;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import io.descoped.support.jpa.primitive.JpaPrimitive;
import org.jboss.weld.injection.spi.ResourceReference;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.jboss.weld.injection.spi.helpers.SimpleResourceReference;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Created by oranheim on 14/02/2017.
 */
public class EntityManagerFactoryResourceReferenceFactory implements ResourceReferenceFactory<EntityManagerFactory> {

    private final String name;
    private final String unitName;

    public EntityManagerFactoryResourceReferenceFactory(final PersistenceUnit persistenceUnitAnnotation) {
        this.name = persistenceUnitAnnotation.name();
        this.unitName = persistenceUnitAnnotation.unitName();
    }

    private boolean isNotNull(String value) {
        return value != null && !"".equals(value);
    }

    private JpaPrimitive getJpaPrimitive() {
        InstanceFactory<DescopedPrimitive> spiInstanceFactory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        return (JpaPrimitive) spiInstanceFactory.instances().get(JpaPrimitive.class).get();
    }

    @Override
    public ResourceReference<EntityManagerFactory> createResource() {
        if (isNotNull(unitName)) {
            if (false) {
                EntityManagerFactory emf = getJpaPrimitive().findEntityManagerFactory(unitName);
                ResourceReference<EntityManagerFactory> factory = new SimpleResourceReference<>(emf);
                return factory;
            } else {
                EntityManagerFactoryResourceReference factory = new EntityManagerFactoryResourceReference(name, unitName);
                return factory;
            }
        }
        throw new DescopedServerException("Unable to create resource reference for EntityManagerFactory: " + unitName);
    }
}
