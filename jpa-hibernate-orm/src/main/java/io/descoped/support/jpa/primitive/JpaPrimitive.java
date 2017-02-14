package io.descoped.support.jpa.primitive;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.inject.Vetoed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by oranheim on 14/02/2017.
 */
@Priority(80)
@PrimitiveModule
@Vetoed
public class JpaPrimitive implements DescopedPrimitive {

    private static final Logger log = LoggerFactory.getLogger(JpaPrimitive.class);

    private Map<String, EntityManagerFactory> entityManagerFactoryMap;

    private boolean hasEntityManagerFactory(String unitName) {
        return entityManagerFactoryMap.containsKey(unitName);
    }

    private EntityManagerFactory createEntityManagerFactory(String unitName) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(unitName);
        entityManagerFactoryMap.put(unitName, entityManagerFactory);
        return entityManagerFactory;
    }

    public EntityManagerFactory findEntityManagerFactory(String unitName) {
        if (hasEntityManagerFactory(unitName)) {
            return entityManagerFactoryMap.get(unitName);
        } else {
            return createEntityManagerFactory(unitName);
        }
    }
    
    public void releaseEntityManagerFactory(String unitName) {
        if (entityManagerFactoryMap.containsKey(unitName)) {
            EntityManagerFactory emf = entityManagerFactoryMap.get(unitName);
            log.trace("Release of {}: {}", (emf.isOpen() ? "Open" : "Closed"), unitName);
            if (emf.isOpen()) {
                emf.close();
            }
            entityManagerFactoryMap.remove(unitName);
        }
    }

    @Override
    public void init() {
        entityManagerFactoryMap = new WeakHashMap<>();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        for (String unitName : entityManagerFactoryMap.keySet()) {
            releaseEntityManagerFactory(unitName);
        }
    }

    @Override
    public void destroy() {
        entityManagerFactoryMap.clear();
        entityManagerFactoryMap = null;
    }

}
