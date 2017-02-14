package io.descoped.support.jdbc.cdi;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import io.descoped.support.jdbc.primitive.DataSourcePrimitive;
import org.jboss.weld.injection.spi.ResourceReference;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;
import org.jboss.weld.injection.spi.helpers.SimpleResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by oranheim on 13/02/2017.
 */
public class DescopedResourceReferenceFactory implements ResourceReferenceFactory<Object> {

    private static final Logger log = LoggerFactory.getLogger(DescopedResourceReferenceFactory.class);
    private final Resource resourceAnno;

    public DescopedResourceReferenceFactory(Resource resourceAnno) {
        this.resourceAnno = resourceAnno;
    }

    private boolean isNotNull(String value) {
        return value != null && !"".equals(value);
    }

    private DataSourcePrimitive getDataSourceLoader() {
        InstanceFactory<DescopedPrimitive> spiInstanceFactory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        return (DataSourcePrimitive) spiInstanceFactory.instances().get(DataSourcePrimitive.class).get();
    }

    private ResourceReference<Object> findResourceObject() {
        ResourceReference<Object> resourceReference;
        if (isNotNull(resourceAnno.mappedName())) {
            javax.sql.DataSource dataSource = getDataSourceLoader().findDataSource(resourceAnno.mappedName());
            resourceReference = new SimpleResourceReference<>(dataSource);

        } else if (isNotNull(resourceAnno.lookup())) {
            try {
                InitialContext ic = new InitialContext();
                DataSource dataSource = (DataSource) ic.lookup(resourceAnno.lookup());
                resourceReference = new SimpleResourceReference<>(dataSource);

            } catch (NamingException e) {
                throw new DescopedServerException("Unable to lookup: " + resourceAnno.lookup());
            }

        } else {
            throw new UnsupportedOperationException("You must use Resource.lookup() or .mappedName() to resolve a DataSource");
        }

        return resourceReference;
    }

    @Override
    public ResourceReference<Object> createResource() {
        return findResourceObject();
    }

}
