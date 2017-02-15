package io.descoped.support.jpa.services;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import io.descoped.support.jpa.primitive.JpaPrimitive;
import org.jboss.weld.injection.spi.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;

/**
 * Created by oranheim on 14/02/2017.
 */
public class EntityManagerFactoryResourceReference implements ResourceReference<EntityManagerFactory> {

    private static final Logger log = LoggerFactory.getLogger(EntityManagerFactoryResourceReference.class);

    private final String name;
    private final String unitName;

    public EntityManagerFactoryResourceReference(final String name, final String unitName) {
        this.name = name;
        this.unitName = unitName;
    }

    private boolean isNotNull(String value) {
        return value != null && !"".equals(value);
    }

    private JpaPrimitive getJpaPrimitive() {
        InstanceFactory<DescopedPrimitive> spiInstanceFactory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        return (JpaPrimitive) spiInstanceFactory.instances().get(JpaPrimitive.class).get();
    }

    @Override
    public EntityManagerFactory getInstance() {
        log.trace("Lookup {} and resolve {}", unitName, getJpaPrimitive().hasEntityManagerFactory(unitName));
        if (isNotNull(unitName)) {
            EntityManagerFactory emf = getJpaPrimitive().findEntityManagerFactory(unitName);
            return emf;
        }
        throw new DescopedServerException("Unable to create resource reference for EntityManagerFactory: " + unitName);
    }

    @Override
    public void release() {
        log.trace("Release: {}", unitName);
        getJpaPrimitive().releaseEntityManagerFactory(unitName);
    }

}
