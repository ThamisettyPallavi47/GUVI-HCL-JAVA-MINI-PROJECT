package com.teamcalendar.model;

import java.util.List;

public class TeamMember {
    private String id;
    private String name;
    private String email;
    private List<String> availableSlots;

    public TeamMember(String id, String name, String email, List<String> slots){
        this.id = id;
        this.name = name;
        this.email = email;
        this.availableSlots = slots;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(List<String> slots) { this.availableSlots = slots; }
}
