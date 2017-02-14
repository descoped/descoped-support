package io.descoped.support.jdbc.cdi;

import org.jboss.weld.injection.spi.ResourceInjectionServices;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Created by oranheim on 13/02/2017.
 */
public class DescopedResourceInjectionServices implements ResourceInjectionServices {

    private static final Logger log = LoggerFactory.getLogger(DescopedResourceInjectionServices.class);


    public DescopedResourceInjectionServices() {
    }

    @Override
    public ResourceReferenceFactory<Object> registerResourceInjectionPoint(InjectionPoint injectionPoint) {
        if (injectionPoint.getAnnotated().isAnnotationPresent(Resource.class)) {
            Resource resourceAnno = injectionPoint.getAnnotated().getAnnotation(Resource.class);
            DescopedResourceReferenceFactory factory = new DescopedResourceReferenceFactory(resourceAnno);
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
