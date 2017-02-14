package io.descoped.support.jdbc.test;

import io.descoped.reflection.proxy.ClassProxy;
import io.descoped.support.jdbc.config.DataSourceLoader;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oranheim on 12/02/2017.
 */
@Ignore
@RunWith(JUnit4.class)
public class JdbcDataSourceConfigTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcDataSourceConfigTest.class);

    private List<String> resolveDatasourceKeys() {
        List<String> keys = new ArrayList<>();
        ConfigResolver.getAllProperties().forEach((k,v) -> {
            if (k.startsWith("jdbc.driver")) {
                String[] split = k.split("\\.");
                keys.add(split[2]);
            }
        });
        return keys;
    }

    private Map<String,String> resolveJdbcProperties(String datasource) {
        Map<String, String> props = new HashMap<>();
        props.put("jdbc.driver", ConfigResolver.resolve(String.format("jdbc.driver.%s", datasource)).getValue());
        props.put("jdbc.jndi", ConfigResolver.resolve(String.format("jdbc.jndi.%s", datasource)).getValue());
        props.put("jdbc.url", ConfigResolver.resolve(String.format("jdbc.url.%s", datasource)).getValue());
        props.put("jdbc.username", ConfigResolver.resolve(String.format("jdbc.username.%s", datasource)).getValue());
        props.put("jdbc.password", ConfigResolver.resolve(String.format("jdbc.password.%s", datasource)).withDefault("").getValue());
        return props;
    }

    private DataSource createDatasource(String driverClass) {
        ClassProxy<DataSource> classProxy = new ClassProxy(driverClass);
        return classProxy.construct().getInstance();
    }

//    @Test
    public void testConfig() throws Exception {
        List<String> datasources = resolveDatasourceKeys();
        for(String datasource : datasources) {
            Map<String, String> props = resolveJdbcProperties(datasource);
            log.trace("Datasource: {}", datasource);
            for(Map.Entry<String,String> entry : props.entrySet()) {
                log.trace(" {}={}", entry.getKey(), entry.getValue());
            }
            String driverClass = props.get("jdbc.driver");
            DataSource ds = createDatasource(driverClass);

            log.trace(" Driver instance: {}", ds.getClass());

        }
    }

    @Test
    public void testDataSourceLoader() throws Exception {
        DataSourceLoader dataSourceLoader = new DataSourceLoader();
        dataSourceLoader.load();
        dataSourceLoader.getDataSourceConfigMap().forEach((k,v) -> {
            log.trace("DataSourceName: {} => {}", k, v.createDatasource());
        });
    }
}
