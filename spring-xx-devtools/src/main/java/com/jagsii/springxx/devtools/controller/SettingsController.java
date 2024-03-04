package com.jagsii.springxx.devtools.controller;

import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.devtools.AppSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SettingsController {
    private final AppSettings appSettings;

    @GetMapping("/settings")
    public R<String> settings() throws IOException {
        return R.success(appSettings.read());
    }
}
