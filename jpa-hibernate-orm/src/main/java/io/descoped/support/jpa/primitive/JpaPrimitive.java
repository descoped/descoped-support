package io.descoped.support.jpa.primitive;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.inject.Vetoed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by oranheim on 14/02/2017.
 */
@Priority(80)
@PrimitiveModule
@Vetoed
public class JpaPrimitive implements DescopedPrimitive {

    private static final Logger log = LoggerFactory.getLogger(JpaPrimitive.class);

    private Map<String, EntityManagerFactory> entityManagerFactoryMap;

    public JpaPrimitive() {
        entityManagerFactoryMap = new ConcurrentHashMap<>();
    }

    public boolean hasEntityManagerFactory(String unitName) {
        return entityManagerFactoryMap.containsKey(unitName);
    }

    private EntityManagerFactory createEntityManagerFactory(String unitName) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(unitName);
//        log.info("Create PersistenceUnit instance: {}@{}", unitName, entityManagerFactory.hashCode());
        entityManagerFactoryMap.put(unitName, entityManagerFactory);
        return entityManagerFactory;
    }

    public EntityManagerFactory findEntityManagerFactory(String unitName) {
        if (hasEntityManagerFactory(unitName)) {
            EntityManagerFactory entityManagerFactory = entityManagerFactoryMap.get(unitName);
//            log.trace("Found PersistenceUnit instance: {}@{}", unitName, entityManagerFactory.hashCode());
            return entityManagerFactory;
        } else {
            return createEntityManagerFactory(unitName);
        }
    }

    public void releaseEntityManagerFactory(String unitName) {
        if (entityManagerFactoryMap.containsKey(unitName)) {
            EntityManagerFactory emf = entityManagerFactoryMap.get(unitName);
            log.trace("Release of {} EntityManagerFactory: {}", (emf.isOpen() ? "(open)" : "(closed)"), unitName);
            if (emf.isOpen()) {
                emf.close();
            }
            entityManagerFactoryMap.remove(unitName);
        }
    }

    private void discoverAndCreatePersistenceUnits() {
        List<ParsedPersistenceXmlDescriptor> units = PersistenceXmlParser.locatePersistenceUnits(Collections.emptyMap());
        for (ParsedPersistenceXmlDescriptor desc : units) {
            String unitName = desc.getName();
            log.trace("Discovered PersistenceUnit: {}", unitName);
            findEntityManagerFactory(unitName);
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void start() {
        discoverAndCreatePersistenceUnits();
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
