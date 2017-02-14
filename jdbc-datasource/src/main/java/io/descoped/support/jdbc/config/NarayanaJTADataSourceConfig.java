package io.descoped.support.jdbc.config;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.reflection.proxy.ObjectProxy;

import javax.sql.DataSource;

/**
 * Created by oranheim on 14/02/2017.
 */
public class NarayanaJTADataSourceConfig extends DataSourceConfig {

    public NarayanaJTADataSourceConfig(String datasourceName) {
        super(datasourceName);
    }

    @Override
    public DataSource createDatasource() throws DescopedServerException {
        ObjectProxy<DataSource> proxy = createDatasourceDriver();
        proxy.method("setJndiName", String.class).invoke(getJndiNameRef());
        proxy.method("setUser", String.class).invoke(getUsername());
        proxy.method("setPassword", String.class).invoke(getPassword());
        return proxy.getInstance();
    }
}
