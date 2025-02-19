package org.example;

import org.example.controller.JetController;
import org.example.controller.UserController;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        initializeServer();
        configureCORS();
        initializeControllers();
    }

    private static void initializeServer() {
        port(8080);
    }

    private static void configureCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With");
            response.type("application/json");
        });
    }

    private static void initializeControllers() {
        new UserController();
        new JetController();
    }
    
}