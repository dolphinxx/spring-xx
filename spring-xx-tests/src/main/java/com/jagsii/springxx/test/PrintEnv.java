package com.jagsii.springxx.test;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrintEnv {
    private final ConfigurableEnvironment env;

    public PrintEnv(ConfigurableEnvironment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        List<MapPropertySource> propertySources = new ArrayList<>();

        env.getPropertySources().forEach(it -> {
            if (it instanceof MapPropertySource/* && it.getName().contains("applicationConfig")*/) {
                propertySources.add((MapPropertySource) it);
            }
        });
        StringBuilder out = new StringBuilder();

        propertySources.stream()
                .map(propertySource -> propertySource.getSource().keySet())
                .flatMap(Collection::stream)
                .distinct()
                .sorted()
                .forEach(key -> out.append(key).append("=").append(env.getProperty(key)).append("\n"));
        System.out.println("************************* ACTIVE APP PROPERTIES ******************************");
        System.out.println(out);
        System.out.println("******************************************************************************");
    }
}
