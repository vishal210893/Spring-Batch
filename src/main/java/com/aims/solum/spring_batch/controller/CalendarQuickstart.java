package com.aims.solum.spring_batch.controller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@Slf4j
public class CalendarQuickstart {

    @Autowired
    private Calendar calendar;

    @GetMapping("/calendar/events")
    public ResponseEntity<ArrayList<LinkedHashMap<String, Object>>> getCalendarEvents() throws IOException {

        ArrayList<LinkedHashMap<String, Object>> eventList = new ArrayList<>();

        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = calendar.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        if (items.isEmpty()) {
            log.info("No upcoming events found.");
            return ResponseEntity.noContent().build();
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                System.out.println("Creation Time :" + event.getCreated());
                System.out.println("Creator :" + event.getCreator().getEmail());
                System.out.println("Description :" + event.getDescription());
                System.out.println("End time :" + event.getEnd().getDateTime());
                System.out.println("Google Meet Link :" + event.getHangoutLink());
                System.out.println("Id :" + event.getId());
                System.out.println("Kind :" + event.getKind());
                System.out.println("Location :" + event.getLocation());
                System.out.println("Organizer :" + event.getOrganizer().getEmail());
                System.out.println("Start Time :" + event.getStart().getDateTime());
                System.out.println("Status :" + event.getStatus());
                System.out.println("Summary :" + event.getSummary());
                System.out.println("Update Time :" + event.getUpdated());

                LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
                hm.put("Creation Time :", event.getCreated().toString());
                hm.put("Creator :", event.getCreator().getEmail());
                hm.put("Description :", event.getDescription());
                hm.put("End time :", event.getEnd().getDateTime().toString());
                hm.put("Google Meet Link :", event.getHangoutLink());
                hm.put("Id :", event.getId());
                hm.put("Kind :", event.getKind());
                hm.put("Location :", event.getLocation());
                hm.put("Organizer :", event.getOrganizer().getEmail());
                hm.put("Start Time :", event.getStart().getDateTime().toString());
                hm.put("Status :", event.getStatus());
                hm.put("Summary :", event.getSummary());
                hm.put("Update Time :", event.getUpdated().toString());
                eventList.add(hm);
            }
        }
        return ResponseEntity.ok().body(eventList);
    }

}