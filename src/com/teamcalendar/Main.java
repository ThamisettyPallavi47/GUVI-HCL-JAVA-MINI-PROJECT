

package com.teamcalendar;

import com.teamcalendar.dao.*;
import com.teamcalendar.service.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        MongoDatabase db = MongoDBConnection.getConnection();
        TeamDAO dao = new TeamDAO();
        CalendarService service = new CalendarService(db);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Team Availability Calendar ===");
            System.out.println("1. Add Member");
            System.out.println("2. Show Members");
            System.out.println("3. Update Member Slots");
            System.out.println("4. Delete Member");
            System.out.println("5. Find Common Availability");
            System.out.println("6. Schedule Meeting");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter ID: ");
                    String id = sc.next();
                    System.out.print("Enter Name: ");
                    String name = sc.next();
                    System.out.print("Enter Email: ");
                    String email = sc.next();
                    System.out.print("Enter slots (comma separated): ");
                    sc.nextLine();
                    String slotStr = sc.nextLine();
                    List<String> slots = Arrays.asList(slotStr.split(","));
                    dao.addMember(id, name, email, slots);
                    break;

                case 2:
                    for (Document doc : dao.getAllMembers())
                        System.out.println(doc.toJson());
                    break;

                case 3:
                    System.out.print("Enter Member ID to update: ");
                    sc.nextLine();
                    String uid = sc.nextLine();
                    System.out.print("Enter new slots (comma separated): ");
                    List<String> newSlots = Arrays.asList(sc.nextLine().split(","));
                    dao.updateSlots(uid, newSlots);
                    break;

                case 4:
                    System.out.print("Enter Member ID to delete: ");
                    sc.nextLine();
                    String did = sc.nextLine();
                    dao.deleteMember(did);
                    break;

                case 5:
                    System.out.print("Enter Member IDs (comma separated): ");
                    sc.nextLine();
                    List<String> ids = Arrays.asList(sc.nextLine().split(","));
                    Set<String> common = service.findCommonAvailability(ids);
                    System.out.println("Common Slots: " + common);
                    break;

                case 6:
                    System.out.print("Enter Slot: ");
                    sc.nextLine();
                    String slot = sc.nextLine();
                    System.out.print("Enter Member IDs (comma separated): ");
                    List<String> mid = Arrays.asList(sc.nextLine().split(","));
                    service.scheduleMeeting(slot, mid);
                    break;

                case 7:
                    System.out.println("Exiting...");
                    System.exit(0);
            }
        }
    }
}


