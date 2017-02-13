package io.descoped.support.jpa.config;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.reflection.proxy.ClassProxy;
import io.descoped.reflection.proxy.ObjectProxy;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oranheim on 13/02/2017.
 */
abstract public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    private final String datasourceName;
    private final Map<String, String> datasourceProperties;

    public DataSourceConfig(String datasourceName) {
        this.datasourceName = datasourceName;
        this.datasourceProperties = resolvePropertyMap();
    }

    protected boolean isJndiNameAlreadyBound(InitialContext context, String name) {
        try {
            context.lookup(name);
        } catch (NamingException e) {
            return false;
        }
        return true;
    }

    private Map<String, String> resolvePropertyMap() {
        Map<String, String> props = new HashMap<>();
        props.put("jdbc.driver", ConfigResolver.resolve(String.format("jdbc.driver.%s", datasourceName)).getValue());
        props.put("jdbc.jndi", ConfigResolver.resolve(String.format("jdbc.jndi.%s", datasourceName)).getValue());
        props.put("jdbc.url", ConfigResolver.resolve(String.format("jdbc.url.%s", datasourceName)).getValue());
        props.put("jdbc.username", ConfigResolver.resolve(String.format("jdbc.username.%s", datasourceName)).getValue());
        props.put("jdbc.password", ConfigResolver.resolve(String.format("jdbc.password.%s", datasourceName)).withDefault("").getValue());
        return props;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    protected ObjectProxy<DataSource> createDatasourceDriver() {
        ClassProxy<DataSource> classProxy = new ClassProxy(getDriverclass());
        return classProxy.construct();
    }

    protected String getDriverclass() {
        return datasourceProperties.get("jdbc.driver");
    }

    protected String getJndiName() {
        return datasourceProperties.get("jdbc.jndi");
    }

    protected String getDataUrl() {
        return datasourceProperties.get("jdbc.url");
    }

    protected String getUsername() {
        return datasourceProperties.get("jdbc.username");
    }

    protected String getPassword() {
        return datasourceProperties.get("jdbc.password");
    }

    abstract public DataSource createDatasource() throws DescopedServerException;

    public void bind(DataSource dataSource) throws DescopedServerException {
        try {
            InitialContext context = new InitialContext();
            String dataSourceJndiName = getJndiName();
            if (!isJndiNameAlreadyBound(context, dataSourceJndiName)) {
//                DataSource instance = createDatasource();
                log.info("Bind {} to {}", datasourceName, dataSourceJndiName);
                context.bind(dataSourceJndiName, dataSource);
            }
        } catch (NamingException e) {
            throw new DescopedServerException(e);
        }
    }

    public void unbind() throws DescopedServerException {
        try {
            InitialContext context = new InitialContext();
            String dataSourceJndiName = getJndiName();
            if (isJndiNameAlreadyBound(context, dataSourceJndiName)) {
                log.info("Unbind {} to {}", datasourceName, dataSourceJndiName);
                context.unbind(dataSourceJndiName);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
