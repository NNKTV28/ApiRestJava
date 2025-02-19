package org.example.controller;

import org.example.config.HibernateUtil;
import org.example.model.User;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spark.Route;
import static spark.Spark.*;

public class UserController {
    private final Gson gson = new Gson();

    public UserController() {
        path("/api", () -> {
            path("/users", () -> {
                get("", getAllUsers);
                get("/:id", getUserById);
                post("", createUser);
                put("/:id", updateUser);
                delete("/:id", deleteUser);
            });
        });
    }

    private final Route getAllUsers = (request, response) -> {
        response.type("application/json");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return gson.toJson(session.createQuery("from User", User.class).list());
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error al obtener usuarios: " + e.getMessage()));
        }
    };

    private final Route getUserById = (request, response) -> {
        response.type("application/json");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long id = Long.parseLong(request.params(":id"));
            User user = session.get(User.class, id);
            if (user == null) {
                response.status(404);
                return gson.toJson(new ErrorResponse("Usuario no encontrado"));
            }
            return gson.toJson(user);
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse("ID inválido"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error al obtener usuario: " + e.getMessage()));
        }
    };

    private final Route createUser = (request, response) -> {
        response.type("application/json");
        try {
            User user = gson.fromJson(request.body(), User.class);
            if (user.getName() == null || user.getEmail() == null) {
                response.status(400);
                return gson.toJson(new ErrorResponse("Nombre y email son requeridos"));
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.persist(user);
                transaction.commit();
                response.status(201);
                return gson.toJson(user);
            }
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error al crear usuario: " + e.getMessage()));
        }
    };

    private final Route updateUser = (request, response) -> {
        response.type("application/json");
        try {
            User updates = gson.fromJson(request.body(), User.class);
            Long id = Long.parseLong(request.params(":id"));

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user == null) {
                    response.status(404);
                    return gson.toJson(new ErrorResponse("Usuario no encontrado"));
                }

                if (updates.getName() != null) user.setName(updates.getName());
                if (updates.getEmail() != null) user.setEmail(updates.getEmail());
                
                session.merge(user);
                transaction.commit();
                return gson.toJson(user);
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse("ID inválido"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error al actualizar usuario: " + e.getMessage()));
        }
    };

    private final Route deleteUser = (request, response) -> {
        response.type("application/json");
        try {
            Long id = Long.parseLong(request.params(":id"));
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user == null) {
                    response.status(404);
                    return gson.toJson(new ErrorResponse("Usuario no encontrado"));
                }
                session.remove(user);
                transaction.commit();
                response.status(204);
                return "";
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse("ID inválido"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error al eliminar usuario: " + e.getMessage()));
        }
    };

    private static class ErrorResponse {
        ErrorResponse(String message) {
        }
    }
}