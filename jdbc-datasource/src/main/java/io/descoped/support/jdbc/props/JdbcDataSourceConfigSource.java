package io.descoped.support.jdbc.props;

import org.apache.deltaspike.core.impl.config.BaseConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by oranheim on 15/02/2017.
 */
public class JdbcDataSourceConfigSource extends BaseConfigSource implements ConfigSource {

    private Map<String,String> props = new LinkedHashMap<>();

    public JdbcDataSourceConfigSource() {
        initOrdinal(getOrdinal());
    }

    @Override
    public int getOrdinal() {
        return 100;
    }

    @Override
    public Map<String, String> getProperties() {
        return props;
    }

    @Override
    public String getPropertyValue(String key) {
        return props.get(key);
    }

    @Override
    public String getConfigName() {
        return "META-INF/jdbc-datasource.properties";
    }

    @Override
    public boolean isScannable() {
        return true;
    }

}
