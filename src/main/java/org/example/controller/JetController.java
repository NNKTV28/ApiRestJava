package org.example.controller;

import org.example.config.HibernateUtil;
import org.example.model.Jet;
import org.example.model.User;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import spark.Route;
import static spark.Spark.*;

public class JetController {
    private final Gson gson = new Gson();

    public JetController() {
        path("/api", () -> {
            path("/users", () -> {
                get("/:userId/jets", getUserJets);
                post("/:userId/jets", addJetToUser);
            });
            path("/jets", () -> {
                get("", getAllJets);
                get("/:id", getJetById);
                post("", createJet);
                put("/:id", updateJet);
                delete("/:id", deleteJet);
            });
        });
    }

    private final Route getAllJets = (request, response) -> {
        response.type("application/json");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return gson.toJson(session.createQuery("from Jet", Jet.class).list());
        }
    };

    private final Route getJetById = (request, response) -> {
        response.type("application/json");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long id = Long.parseLong(request.params(":id"));
            Jet jet = session.get(Jet.class, id);
            if (jet == null) {
                response.status(404);
                return gson.toJson(new ErrorResponse("Jet not found"));
            }
            return gson.toJson(jet);
        }
    };

    private final Route createJet = (request, response) -> {
        response.type("application/json");
        Jet jet = gson.fromJson(request.body(), Jet.class);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(jet);
            transaction.commit();
            response.status(201);
            return gson.toJson(jet);
        }
    };

    private final Route updateJet = (request, response) -> {
        response.type("application/json");
        Jet updates = gson.fromJson(request.body(), Jet.class);
        Long id = Long.parseLong(request.params(":id"));
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Jet jet = session.get(Jet.class, id);
            if (jet == null) {
                response.status(404);
                return gson.toJson(new ErrorResponse("Jet not found"));
            }
            
            if (updates.getModel() != null) jet.setModel(updates.getModel());
            if (updates.getManufacturer() != null) jet.setManufacturer(updates.getManufacturer());
            if (updates.getMaxSpeed() != null) jet.setMaxSpeed(updates.getMaxSpeed());
            if (updates.getRange() != null) jet.setRange(updates.getRange());
            if (updates.getArmament() != null) jet.setArmament(updates.getArmament());
            
            session.merge(jet);
            transaction.commit();
            return gson.toJson(jet);
        }
    };

    private final Route deleteJet = (request, response) -> {
        response.type("application/json");
        Long id = Long.parseLong(request.params(":id"));
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Jet jet = session.get(Jet.class, id);
            if (jet == null) {
                response.status(404);
                return gson.toJson(new ErrorResponse("Jet not found"));
            }
            session.remove(jet);
            transaction.commit();
            response.status(204);
            return "";
        }
    };

    private final Route getUserJets = (request, response) -> {
        response.type("application/json");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long userId = Long.parseLong(request.params("userId"));
            User user = session.get(User.class, userId);
            if (user == null) {
                response.status(404);
                return gson.toJson(new ErrorResponse("User not found"));
            }
            return gson.toJson(user.getClass());
        }
    };

    private final Route addJetToUser = (request, response) -> {
        response.type("application/json");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            Long userId = Long.parseLong(request.params("userId"));
            User user = session.get(User.class, userId);
            if (user == null) {
                response.status(404);
                return gson.toJson(new ErrorResponse("User not found"));
            }

            Jet jet = gson.fromJson(request.body(), Jet.class);
            jet.setOwner(user);
            session.persist(jet);
            
            transaction.commit();
            response.status(201);
            return gson.toJson(jet);
        }
    };

    private static class ErrorResponse {
        ErrorResponse(String message) {
        }
    }
}
