package com.bookstore;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.BindException;
import java.net.URI;

public class Main {
    public static final String BASE_URI_FORMAT = "http://localhost:%d/api/";
    public static final int DEFAULT_PORT = 8080;
    public static final int MAX_PORT_ATTEMPTS = 10;  // Try 10 ports before giving up

    public static HttpServer startServer() {
        // Register all resources and exception mappers
        final ResourceConfig rc = new ResourceConfig()
                .packages("com.bookstore.resource", "com.bookstore.exception");

        // Try different ports if the default is in use
        HttpServer server = null;
        int port = DEFAULT_PORT;
        int attempts = 0;

        while (server == null && attempts < MAX_PORT_ATTEMPTS) {
            try {
                String baseUri = String.format(BASE_URI_FORMAT, port);
                server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);
                System.out.println("Server started on port " + port);
                return server;
            } catch (Exception e) {
                if (e.getCause() instanceof BindException) {
                    System.out.println("Port " + port + " is in use, trying next port...");
                    port++;
                    attempts++;
                } else {
                    // For other types of exceptions, rethrow
                    throw e;
                }
            }
        }

        throw new RuntimeException("Could not start server after trying " + MAX_PORT_ATTEMPTS + " ports");
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        System.out.println("Bookstore API started at " + server.getListener("grizzly").getHost() + ":"
                + server.getListener("grizzly").getPort() + "/api/");
        System.out.println("Press enter to stop the server...");
        System.in.read();
        server.shutdownNow();
    }
}