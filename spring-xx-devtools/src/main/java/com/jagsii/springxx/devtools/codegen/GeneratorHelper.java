package com.jagsii.springxx.devtools.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GeneratorHelper {
    public static String renderImports(Collection<String> imports, String packageName) {
        List<String> list = new ArrayList<>(imports);
        Collections.sort(list);
        StringBuilder buff = new StringBuilder();
        for (String im : list) {
            // skip same/java.lang package
            String pkg = im.substring(0, im.lastIndexOf("."));
            if (pkg.equals(packageName) || pkg.startsWith("java.lang")) {
                continue;
            }
            buff.append("import ").append(im).append(";\n");
        }
        return buff.toString();
    }

    public static String packageToPath(String packageName) {
        return packageName.replace('.', File.separatorChar);
    }
}
