package com.jagsii.springxx.config;

import com.jagsii.springxx.common.security.CurrentUserArgumentResolver;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;
    private final WebProperties webProperties;
    private final WebMvcProperties webMvcProperties;

    public WebMvcConfiguration(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder, WebProperties webProperties, WebMvcProperties webMvcProperties) {
        this.jackson2ObjectMapperBuilder = jackson2ObjectMapperBuilder;
        this.webProperties = webProperties;
        this.webMvcProperties = webMvcProperties;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(jackson2ObjectMapperBuilder.build()));
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(webMvcProperties.getStaticPathPattern()).addResourceLocations(webProperties.getResources().getStaticLocations());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver());
    }
}