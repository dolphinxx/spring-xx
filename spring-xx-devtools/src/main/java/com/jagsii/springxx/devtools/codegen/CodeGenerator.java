package com.jagsii.springxx.devtools.codegen;

import lombok.SneakyThrows;
import org.apache.commons.io.output.StringBuilderWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.util.List;

public abstract class CodeGenerator<DATA> {
    @Autowired
    protected FreeMarkerConfig freeMarkerConfig;

    @SneakyThrows
    protected String renderTemplate(String template, Object model) {
        StringBuilderWriter out = new StringBuilderWriter();
        freeMarkerConfig.getConfiguration().getTemplate(template).process(model, out);
        return out.toString();
    }

    public abstract List<GeneratedFile> generate(DATA data);
}
