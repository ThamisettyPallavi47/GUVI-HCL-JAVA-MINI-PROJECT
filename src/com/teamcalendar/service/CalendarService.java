package com.teamcalendar.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.*;

public class CalendarService {
    private final MongoDatabase db;

    public CalendarService(MongoDatabase db) {
        this.db = db;
    }

    public Set<String> findCommonAvailability(List<String> memberIds) {
        MongoCollection<Document> members = db.getCollection("team_members");
        Set<String> common = new HashSet<>();

        for (String id : memberIds) {
            Document member = members.find(new Document("_id", id)).first();
            if (member == null) continue;
            // List<String> slots = (List<String>) member.get("availableSlots");
            Object slotObj = member.get("availableSlots");
List<String> slots = new ArrayList<>();

if (slotObj instanceof List<?>) {
    for (Object item : (List<?>) slotObj) {
        if (item instanceof String) {
            slots.add((String) item);
        }
    }
}

            if (common.isEmpty()) common.addAll(slots);
            else common.retainAll(slots);
        }
        return common;
    }

    public void scheduleMeeting(String slot, List<String> memberIds) {
        MongoCollection<Document> meetings = db.getCollection("meetings");
        Document meeting = new Document("meetingId", UUID.randomUUID().toString())
                .append("timeSlot", slot)
                .append("participantIds", memberIds);
        meetings.insertOne(meeting);
        System.out.println("✅ Meeting scheduled successfully in slot " + slot);
    }
}
