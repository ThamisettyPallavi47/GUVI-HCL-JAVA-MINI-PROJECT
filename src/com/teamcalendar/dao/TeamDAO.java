package com.teamcalendar.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.*;

public class TeamDAO {
    MongoDatabase db = MongoDBConnection.getConnection();
    MongoCollection<Document> memberCollection = db.getCollection("team_members");

    // CREATE
    public void addMember(String id, String name, String email, List<String> slots) {
        Document doc = new Document("_id", id)
                .append("name", name)
                .append("email", email)
                .append("availableSlots", slots);
        memberCollection.insertOne(doc);
        System.out.println("✅ Member added successfully!");
    }

    // READ
    public List<Document> getAllMembers() {
        return memberCollection.find().into(new ArrayList<>());
    }

    // UPDATE
    public void updateSlots(String id, List<String> newSlots) {
        memberCollection.updateOne(new Document("_id", id),
                new Document("$set", new Document("availableSlots", newSlots)));
        System.out.println("✅ Slots updated!");
    }

    // DELETE
    public void deleteMember(String id) {
        memberCollection.deleteOne(new Document("_id", id));
        System.out.println("✅ Member deleted!");
    }
}
