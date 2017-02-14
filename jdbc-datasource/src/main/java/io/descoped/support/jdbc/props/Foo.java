package io.descoped.support.jdbc.props;

import org.apache.deltaspike.core.spi.config.ConfigSource;

import java.util.Map;

/**
 * Created by oranheim on 12/02/2017.
 */
public class Foo implements ConfigSource {

    @Override
    public int getOrdinal() {
        return 0;
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    @Override
    public String getPropertyValue(String key) {
        return null;
    }

    @Override
    public String getConfigName() {
        return null;
    }

    @Override
    public boolean isScannable() {
        return false;
    }
}
