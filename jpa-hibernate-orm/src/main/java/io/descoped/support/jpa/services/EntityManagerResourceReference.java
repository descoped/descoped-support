package io.descoped.support.jpa.services;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import io.descoped.support.jpa.primitive.JpaPrimitive;
import org.jboss.weld.injection.spi.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.SynchronizationType;

/**
 * Created by oranheim on 14/02/2017.
 */
public class EntityManagerResourceReference implements ResourceReference<EntityManager> {

    private static final Logger log = LoggerFactory.getLogger(EntityManagerResourceReference.class);

    private static boolean ENABLE_TX_PROXY = false;

    private final PersistenceContext persistenceContextAnnotation;
    private String unitName;
    private EntityManager entityManager;

    public EntityManagerResourceReference(PersistenceContext persistenceContextAnnotation) {
        this.unitName = persistenceContextAnnotation.unitName();
        this.persistenceContextAnnotation = persistenceContextAnnotation;
    }

    private boolean isNotNull(String value) {
        return value != null && !"".equals(value);
    }

    private JpaPrimitive getJpaPrimitive() {
        InstanceFactory<DescopedPrimitive> spiInstanceFactory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        return (JpaPrimitive) spiInstanceFactory.instances().get(JpaPrimitive.class).get();
    }

    @Override
    public EntityManager getInstance() {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory = getJpaPrimitive().findEntityManagerFactory(unitName);
            if (ENABLE_TX_PROXY && persistenceContextAnnotation.synchronization().equals(SynchronizationType.SYNCHRONIZED)) {
                log.trace("SynchronizationType: {}", persistenceContextAnnotation.synchronization());
                entityManager = JtaEntityManagerProxy.newInstance(entityManagerFactory.createEntityManager());
            } else {
                entityManager = entityManagerFactory.createEntityManager();
            }
        }
        return entityManager;
    }

    @Override
    public void release() {
        log.trace("Release of {} EntityManager: {}", (entityManager.isOpen() ? "(open)" : "(closed)"), unitName);
        if (entityManager.isOpen()) {
            entityManager.close();
        }
        entityManager = null;
    }
}
