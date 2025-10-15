package com.teamcalendar.model;

import java.util.List;

public class Meeting {
    private String meetingId;
    private List<String> participantIds;
    private String timeSlot;

    public Meeting(String meetingId, List<String> participantIds, String timeSlot){
        this.meetingId = meetingId;
        this.participantIds = participantIds;
        this.timeSlot = timeSlot;
    }

    public String getMeetingId() { return meetingId; }
    public List<String> getParticipantIds() { return participantIds; }
    public String getTimeSlot() { return timeSlot; }
}
