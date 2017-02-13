package io.descoped.support.jpa.config;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.reflection.proxy.ObjectProxy;

import javax.sql.DataSource;

/**
 * Created by oranheim on 13/02/2017.
 */
public class H2Jdbc14DataSourceConfig extends DataSourceConfig {

    public H2Jdbc14DataSourceConfig(String dataSourceName) {
        super(dataSourceName);
    }

    @Override
    public DataSource createDatasource() throws DescopedServerException {
        ObjectProxy<DataSource> proxy = createDatasourceDriver();
        proxy.method("setURL", String.class).invoke(getDataUrl());
        proxy.method("setUser", String.class).invoke(getUsername());
        proxy.method("setPassword", String.class).invoke(getPassword());
        DataSource instance = proxy.getInstance();
        return instance;
    }

}
