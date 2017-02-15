package io.descoped.deltaspike;

import io.descoped.reflection.proxy.ClassProxy;
import io.descoped.reflection.proxy.ObjectProxy;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.spi.config.ConfigSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * This class scans the classpath for custom deltaspike config properties and pre-loads them before the CDI container starts.
 * It is useful when you have to use ConfigResolver before the CDI environment has become active.
 *
 * Created by oranheim on 15/02/2017.
 */
public class NonCdiConfigSourceLoader<T extends ConfigSource> {

    private Class<T> typedClass;

    public NonCdiConfigSourceLoader(final Class<T> typedClass) {
        this.typedClass = typedClass;
    }

    private ObjectProxy<T> getConfigSourceProxy() {
        ClassProxy<T> classProxy = new ClassProxy(typedClass);
        ObjectProxy<T> instanceProxy = classProxy.construct();
        return instanceProxy;
    }

    private T asMap(String properties) throws IOException {
        Properties props = new Properties();
        props.load(new StringReader(properties));

        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }

        ObjectProxy<T> instanceProxy = getConfigSourceProxy();
        Map<String,String> propertiesMethod = (Map<String, String>) instanceProxy.method("getProperties").invoke();
        propertiesMethod.putAll(map);
        
        return instanceProxy.getInstance();
    }

    public void initialize() {
        ObjectProxy<T> instanceProxy = getConfigSourceProxy();
        String configFilename = (String) instanceProxy.method("getConfigName").invoke();

        Map<String, T> buffers = new LinkedHashMap<>();
        new FastClasspathScanner()
                .matchFilenamePath(configFilename, ((templatePath, bytes) -> buffers.put(templatePath, asMap(new String(bytes, "UTF-8")))))
                .scan();

        List<ConfigSource> sources = new ArrayList<>();
        sources.addAll(buffers.values());
        ConfigResolver.addConfigSources(sources);
    }
}
