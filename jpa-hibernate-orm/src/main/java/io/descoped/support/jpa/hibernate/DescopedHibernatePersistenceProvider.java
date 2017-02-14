package io.descoped.support.jpa.hibernate;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import java.util.Map;

/**
 * Created by oranheim on 14/02/2017.
 */
public class DescopedHibernatePersistenceProvider extends HibernatePersistenceProvider {

    public DescopedHibernatePersistenceProvider() {
    }

//    @Override
//    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
//        System.out.println("---------------------------------------------------------------------------------------------------------------------> 1");
//        return super.createEntityManagerFactory(persistenceUnitName, properties);
//    }
//
//    @Override
//    protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilderOrNull(String persistenceUnitName, Map properties) {
//        System.out.println("---------------------------------------------------------------------------------------------------------------------> 8");
//        return super.getEntityManagerFactoryBuilderOrNull(persistenceUnitName, properties);
//    }

    @Override
    protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(PersistenceUnitDescriptor persistenceUnitDescriptor, Map integration, ClassLoader providedClassLoader) {
//        System.out.println("---------------------------------------------------------------------------------------------------------------------> 3");
        EntityManagerFactoryBuilderImpl builder = (EntityManagerFactoryBuilderImpl) super.getEntityManagerFactoryBuilder(persistenceUnitDescriptor, integration, providedClassLoader);
//        SessionFactoryImpl fa = (SessionFactoryImpl) builder.build();
//        fa.
//        builder.getConfigurationValues().forEach((k,v) -> {
//            System.out.println("----> " + k + "  " + v);
//        } );
        return builder;
    }

}
