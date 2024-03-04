package com.jagsii.springxx.devtools;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class AppSettings {
    // --dataDir=..
    @Value("${dataDir:}")
    private String dataDir;
    private Path settingsPath;

    @PostConstruct
    public void init() {
        Path dataPath;
        if (StringUtils.hasText(dataDir)) {
            dataPath = Paths.get(dataDir);
        } else {
            dataPath = Paths.get(System.getProperty("user.home"), ".springxx");
        }
        File dir = dataPath.toFile();
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        settingsPath = dataPath.resolve("settings.json");
    }

    public String read() throws IOException {
        if (settingsPath.toFile().exists()) {
            return new String(Files.readAllBytes(settingsPath), StandardCharsets.UTF_8);
        } else {
            return "{\"projects\":[]}";
        }
    }

    public void save(String settings) throws IOException {
        Files.write(settingsPath, settings.getBytes(StandardCharsets.UTF_8));
    }
}
