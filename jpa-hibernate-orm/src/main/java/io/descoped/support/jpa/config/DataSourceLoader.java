package io.descoped.support.jpa.config;

import org.apache.deltaspike.core.api.config.ConfigResolver;

import java.util.*;

/**
 * Created by oranheim on 13/02/2017.
 */
public class DataSourceLoader {

    private List<String> dataSourceNames;
    private Map<String, DataSourceConfig> dataSourceConfigMap = new LinkedHashMap<>();

    public DataSourceLoader() {
        dataSourceNames = resolveDatasourceKeys();
    }

    private List<String> resolveDatasourceKeys() {
        List<String> keys = new ArrayList<>();
        ConfigResolver.getAllProperties().forEach((k, v) -> {
            if (k.startsWith("jdbc.driver")) {
                String[] split = k.split("\\.");
                keys.add(split[2]);
            }
        });
        return keys;
    }

    private String resolveDataSourceClass(String dataSourceName) {
        return ConfigResolver.resolve(String.format("jdbc.driver.%s", dataSourceName)).getValue();
    }

    public void load() {
        if (!dataSourceConfigMap.isEmpty()) return;

        for(String dataSourceName : dataSourceNames) {
            String dataSourceClass = resolveDataSourceClass(dataSourceName);
            DataSourceConfig dataSourceConfig = createDataSourceConfig(dataSourceName, dataSourceClass);
            dataSourceConfigMap.put(dataSourceName, dataSourceConfig);
        }
    }

    private DataSourceConfig createDataSourceConfig(String dataSourceName, String dataSourceClass) {
        if ("org.h2.jdbcx.JdbcDataSource".equals(dataSourceClass)) {
            return new H2Jdbc14DataSourceConfig(dataSourceName);

        } else if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(dataSourceClass)) {
            return new MySQLJdbc51DataSourceConfig(dataSourceName);
        } else {
            throw new UnsupportedOperationException("Unsupported DataSource: " + dataSourceClass);
        }
    }

    public Set<String> getDataSourceNames() {
        return dataSourceConfigMap.keySet();
    }

    public List<DataSourceConfig> getDataSourceConfigList() {
        return new ArrayList<>(dataSourceConfigMap.values());
    }

    public Map<String, DataSourceConfig> getDataSourceConfigMap() {
        return dataSourceConfigMap;
    }

}
