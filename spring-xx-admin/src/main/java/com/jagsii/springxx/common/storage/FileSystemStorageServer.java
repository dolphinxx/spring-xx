package com.jagsii.springxx.common.storage;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Run a simple debug Server to serve a local folder.
 */
@Slf4j
public class FileSystemStorageServer {
    private static final Map<String, String> contentTypes = new HashMap<>();

    static {
        contentTypes.put("txt", MediaType.TEXT_PLAIN_VALUE);
        contentTypes.put("html", MediaType.TEXT_HTML_VALUE);
        contentTypes.put("jpg", MediaType.IMAGE_JPEG_VALUE);
        contentTypes.put("png", MediaType.IMAGE_PNG_VALUE);
        contentTypes.put("gif", MediaType.IMAGE_GIF_VALUE);
        contentTypes.put("json", MediaType.APPLICATION_JSON_VALUE);
        contentTypes.put("css", "text/css");
        contentTypes.put("js", "text/javascript");
        contentTypes.put("ico", "image/x-icon");
    }

    private final int port;
    private final String prefix;
    private final Path location;

    public FileSystemStorageServer(int port, String prefix, String location) {
        this.port = port;
        this.prefix = prefix;
        this.location = Paths.get(location);
    }

    @SneakyThrows
    public void start() {
        InetSocketAddress host = new InetSocketAddress("localhost", port);
        HttpServer server = HttpServer.create(host, 0);
        server.createContext("/", this::handleRequest);
        server.start();
        log.info("Serving files in {} at http://localhost:{}{}", location, port, prefix);
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        log.info("GET {}", uri);
        String path = uri.getPath();
        Path local = null;
        if (path.startsWith(prefix)) {
            local = location.resolve(path.substring(prefix.length()));
        }
        if (local != null && Files.exists(local)) {
            String filename = local.getFileName().toString();
            int pos = filename.indexOf(".");
            if (pos > 0) {
                String ext = filename.substring(pos + 1).toLowerCase(Locale.ENGLISH);
                if (contentTypes.containsKey(ext)) {
                    exchange.getResponseHeaders().add("Content-Type", contentTypes.get(ext));
                }
            }
            exchange.sendResponseHeaders(200, Files.size(local));
            try (OutputStream out = exchange.getResponseBody()) {
                Files.copy(local, out);
            }
        } else {
            String response = "File not found " + uri;
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
