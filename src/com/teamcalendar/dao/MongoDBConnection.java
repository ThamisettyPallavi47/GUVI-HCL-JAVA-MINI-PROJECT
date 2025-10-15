package com.teamcalendar.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "team_calendar";
    private static MongoDatabase database;

    public static MongoDatabase getConnection() {
        if (database == null) {
            MongoClient client = MongoClients.create(CONNECTION_STRING);
            database = client.getDatabase(DATABASE_NAME);
        }
        return database;
    }
}
