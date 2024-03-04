package com.jagsii.springxx.devtools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        FreeMarkerAutoConfiguration.class,
})
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        if (ServletWebServerApplicationContext.class.isAssignableFrom(context.getClass())) {
            int port = ((ServletWebServerApplicationContext) context).getWebServer().getPort();
            System.out.println("Local:\thttp://localhost:" + port + "/");
        }
    }
}
