package io.descoped.support.jpa.hibernate;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oranheim on 14/02/2017.
 */
public class DescopedHibernatePersistenceProvider extends HibernatePersistenceProvider {

    private static final Logger log = LoggerFactory.getLogger(DescopedHibernatePersistenceProvider.class);

    public DescopedHibernatePersistenceProvider() {
    }

    private String[] resolveJpaScanEntityBeansInPackages() {
        ConfigResolver.TypedResolver<String> scanPackageConfig = ConfigResolver.resolve("jpa.scan.packages").as(String.class).withDefault("io.descoped");
        String[] scanPackages = scanPackageConfig.getValue().split("\\,");
        return scanPackages;
    }

    private List<String> scanManagedClasses() {
        List<String> classNamesList = new ArrayList<>();
        String[] scanPackages = resolveJpaScanEntityBeansInPackages();
        log.info("Scan EntityBeans in packages: {}", new Object[]{scanPackages});
        new FastClasspathScanner(scanPackages)
                .matchClassesWithAnnotation(Entity.class, c -> classNamesList.add(c.getName()))
                .scan();
        return classNamesList;
    }

    @Override
    protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(PersistenceUnitDescriptor persistenceUnitDescriptor, Map integration, ClassLoader providedClassLoader) {
        if (persistenceUnitDescriptor.getManagedClassNames().isEmpty()) {
            List<String> entityBeans = scanManagedClasses();
            persistenceUnitDescriptor.getManagedClassNames().addAll(entityBeans);
        }
        return super.getEntityManagerFactoryBuilder(persistenceUnitDescriptor, integration, providedClassLoader);
    }

}
