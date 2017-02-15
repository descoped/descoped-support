package io.descoped.support.jdbc.props;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by oranheim on 15/02/2017.
 */
@ApplicationScoped
public class JdbcDataSourcePropertyFileConfig implements PropertyFileConfig {

    @Override
    public String getPropertyFileName() {
        return "META-INF/jdbc-datasource.properties";
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}
