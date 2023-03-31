package com.aims.solum.spring_batch.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class ScheduleTest {

    @Scheduled(fixedDelayString = "2000")
    public void launchingGateway() {
        System.out.println("Hello");
    }

}
