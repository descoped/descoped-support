package io.descoped.support.jdbc.primitive;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import io.descoped.deltaspike.NonCdiConfigSourceLoader;
import io.descoped.support.jdbc.config.DataSourceConfig;
import io.descoped.support.jdbc.config.DataSourceLoader;
import io.descoped.support.jdbc.props.JdbcDataSourceConfigSource;

import javax.annotation.Priority;
import javax.enterprise.inject.Vetoed;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by oranheim on 13/02/2017.
 */
@Priority(70)
@PrimitiveModule
@Vetoed
public class DataSourcePrimitive implements DescopedPrimitive {

    private DataSourceLoader dataSourceLoader;
    private Map<String, DataSource> dataSourceMap;

    public DataSourcePrimitive() {
        dataSourceMap = new ConcurrentHashMap<>(); // todo: consider if this should be weak
    }

    public DataSourceLoader getDataSourceLoader() {
        return dataSourceLoader;
    }

    public javax.sql.DataSource findDataSource(String dataSourceName) {
        return dataSourceMap.get(dataSourceName);
    }

    @Override
    public void init() {
    }

    @Override
    public void start() {
        new NonCdiConfigSourceLoader<>(JdbcDataSourceConfigSource.class).initialize();
        dataSourceLoader = new DataSourceLoader();
        dataSourceLoader.load();
        for(DataSourceConfig dataSourceConfig : dataSourceLoader.getDataSourceConfigList()) {
            DataSource dataSource = dataSourceConfig.createDatasource();
            dataSourceConfig.bind(dataSource);
            dataSourceMap.put(dataSourceConfig.getDatasourceName(), dataSource);
        }
    }

    @Override
    public void stop() {
        for(DataSourceConfig dataSourceConfig : dataSourceLoader.getDataSourceConfigList()) {
            dataSourceConfig.unbind();
            dataSourceMap.clear();
            dataSourceMap = new ConcurrentHashMap<>();
        }
    }

    @Override
    public void destroy() {
        dataSourceLoader.getDataSourceConfigMap().clear();
        dataSourceLoader = null;
    }
}
