package io.descoped.support.jdbc.props;

import org.apache.deltaspike.core.spi.config.ConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSourceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oranheim on 15/02/2017.
 */
public class JdbcDataSourceConfigSourceProvider implements ConfigSourceProvider {

    @Override
    public List<ConfigSource> getConfigSources() {
        List<ConfigSource> sources = new ArrayList<>();
        sources.add(new JdbcDataSourceConfigSource());
        return sources;
    }

}
