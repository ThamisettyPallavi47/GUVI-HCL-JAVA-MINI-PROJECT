package com.teamcalendar.api;

import com.teamcalendar.dao.*;
import com.teamcalendar.service.*;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;
import org.bson.Document;
import com.google.gson.Gson;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class Server {
    private static final int PORT = 8080;
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        MongoDatabase db = MongoDBConnection.getConnection();
        TeamDAO dao = new TeamDAO();
        CalendarService service = new CalendarService(db);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println("🚀 Server running on http://localhost:" + PORT);

        // 1️⃣ Add Member
        server.createContext("/addMember", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, Object> data = gson.fromJson(body, Map.class);
                dao.addMember((String)data.get("id"), (String)data.get("name"), (String)data.get("email"), (List<String>)data.get("availableSlots"));
                sendResponse(exchange, "Member added successfully!");
            }
        });

        // 2️⃣ Show Members
        server.createContext("/members", exchange -> {
            List<Document> members = dao.getAllMembers();
            sendJson(exchange, members);
        });

        // 3️⃣ Update Slots
        server.createContext("/updateSlots", exchange -> {
            if ("PUT".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, Object> data = gson.fromJson(body, Map.class);
                dao.updateSlots((String)data.get("id"), (List<String>)data.get("availableSlots"));
                sendResponse(exchange, "Slots updated!");
            }
        });

        // 4️⃣ Delete Member
        server.createContext("/deleteMember", exchange -> {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String id = query.split("=")[1];
                dao.deleteMember(id);
                sendResponse(exchange, "Member deleted!");
            }
        });

        // 5️⃣ Common Availability
        server.createContext("/commonAvailability", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, Object> data = gson.fromJson(body, Map.class);
                List<String> ids = (List<String>) data.get("memberIds");
                Set<String> common = service.findCommonAvailability(ids);
                sendJson(exchange, common);
            }
        });

        // 6️⃣ Schedule Meeting
        server.createContext("/scheduleMeeting", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, Object> data = gson.fromJson(body, Map.class);
                service.scheduleMeeting((String)data.get("slot"), (List<String>)data.get("memberIds"));
                sendResponse(exchange, "Meeting scheduled!");
            }
        });

        server.start();
    }

    private static void sendResponse(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(200, message.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }

    private static void sendJson(HttpExchange exchange, Object data) throws IOException {
        String json = gson.toJson(data);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
}
