package com.jagsii.springxx.devtools.codegen;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(FreeMarkerProperties.class)
public class FreemarkerConfig implements BeanPostProcessor {
    @Bean
    public FreeMarkerConfigurer freeMarkerConfiguration(FreeMarkerProperties properties) throws TemplateModelException {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPaths(properties.getTemplateLoaderPath());
        configurer.setPreferFileSystemAccess(properties.isPreferFileSystemAccess());
        configurer.setDefaultEncoding(properties.getCharsetName());
        Properties settings = new Properties();
        settings.put("recognize_standard_file_extensions", "true");
        settings.putAll(properties.getSettings());
        configurer.setFreemarkerSettings(settings);
        configurer.setPreferFileSystemAccess(false);
        return configurer;
    }

    private Map<String, TemplateModel> getSharedVariables(BeansWrapper config) {
        Map<String, TemplateModel> sharedVariables = new HashMap<>();
        // Add static support, statics['a.b.C'].d()
        TemplateModel statics = config.getStaticModels();
        sharedVariables.put("statics", statics);
        return sharedVariables;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (FreeMarkerConfigurer.class.equals(bean.getClass())) {
            freemarker.template.Configuration configuration = ((FreeMarkerConfigurer) bean).getConfiguration();
            BeansWrapper objectWrapper = (BeansWrapper) configuration.getObjectWrapper();
            try {
                configuration.setSharedVariables(getSharedVariables(objectWrapper));
            } catch (TemplateModelException e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }
}
