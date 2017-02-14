package io.descoped.support.jdbc.config;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.reflection.proxy.ObjectProxy;

import javax.sql.DataSource;

/**
 * Created by oranheim on 13/02/2017.
 */
public class MySQLDataSourceConfig extends DataSourceConfig {

    public MySQLDataSourceConfig(String dataSourceName) {
        super(dataSourceName);
    }

    @Override
    public DataSource createDatasource() throws DescopedServerException {
        ObjectProxy<DataSource> proxy = createDatasourceDriver();
        proxy.method("setUrl", String.class).invoke(getDataUrl());
        proxy.method("setUser", String.class).invoke(getUsername());
        proxy.method("setPassword", String.class).invoke(getPassword());
        return proxy.getInstance();
    }

}
