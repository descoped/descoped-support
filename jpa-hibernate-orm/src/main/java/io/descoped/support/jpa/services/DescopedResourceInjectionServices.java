package io.descoped.support.jpa.services;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import io.descoped.support.jpa.primitive.DataSourcePrimitive;
import org.jboss.weld.injection.spi.ResourceInjectionServices;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.sql.DataSource;

/**
 * Created by oranheim on 13/02/2017.
 */
public class DescopedResourceInjectionServices implements ResourceInjectionServices {

    private static final Logger log = LoggerFactory.getLogger(DescopedResourceInjectionServices.class);
    private final DataSourcePrimitive dataSourcePrimitive;


    public DescopedResourceInjectionServices() {
        InstanceFactory<DescopedPrimitive> spiInstanceFactory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        dataSourcePrimitive = (DataSourcePrimitive) spiInstanceFactory.instances().get(DataSourcePrimitive.class).get();
    }

    @Override
    public ResourceReferenceFactory<Object> registerResourceInjectionPoint(InjectionPoint injectionPoint) {
        if (injectionPoint.getAnnotated().isAnnotationPresent(Resource.class)) {
            Resource resourceAnno = injectionPoint.getAnnotated().getAnnotation(Resource.class);
            DescopedResourceReferenceFactory<DataSource> factory = new DescopedResourceReferenceFactory<>(resourceAnno);
            return factory;
        }
        return null;
    }

    @Override
    public ResourceReferenceFactory<Object> registerResourceInjectionPoint(String jndiName, String mappedName) {
        throw new UnsupportedOperationException("registerResourceInjectionPoint(String jndiName, String mappedName) has NOT been implemented!");
    }

    @Override
    public Object resolveResource(InjectionPoint injectionPoint) {
        throw new UnsupportedOperationException("resolveResource was deprecated in Weld 2.x!");
    }

    @Override
    public Object resolveResource(String jndiName, String mappedName) {
        throw new UnsupportedOperationException("resolveResource was deprecated in Weld 2.x!");
    }

    @Override
    public void cleanup() {

    }
}
