package com.aims.solum.spring_batch.controller;

import com.aims.solum.spring_batch.model.TagScheduleTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@Slf4j
public class RepeatScheduleController {

    @PostMapping("/repeatSchedule")
    public ResponseEntity<String> handle(@RequestBody List<TagScheduleTime> tagScheduleTime) throws Exception {

        int MAX_SCHEDULE_SIZE = 24;
        int SCHEDULE_PACKAGE_SIZE = 9;
        int scheduleCnt = (tagScheduleTime.size() > MAX_SCHEDULE_SIZE) ? MAX_SCHEDULE_SIZE : tagScheduleTime.size();

        int enable = 1;

        ZoneId zone = ZoneId.of("UTC");
        final LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        ZoneOffset zoneOffset = currentTime.atZone(zone).getOffset();
        long startUTC = currentTime.toEpochSecond(zoneOffset);

        byte[] scheduleData = new byte[5 + (scheduleCnt * 9)];

        scheduleData[0] = (byte) ((short) enable);
        scheduleData[1] = ((byte) (startUTC >> 24));
        scheduleData[2] = ((byte) (startUTC >> 16));
        scheduleData[3] = ((byte) (startUTC >> 8));
        scheduleData[4] = ((byte) startUTC);

        int i = 0;
        for (TagScheduleTime scheduleInfo : tagScheduleTime) {
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 5] = (byte) ((short) scheduleInfo.getDisplayPage());
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 6] = ((byte) (scheduleInfo.getEndTimeOffset() >> 24));
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 7] = ((byte) (scheduleInfo.getEndTimeOffset() >> 16));
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 8] = ((byte) (scheduleInfo.getEndTimeOffset() >> 8));
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 9] = ((byte) scheduleInfo.getEndTimeOffset());
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 10] = ((byte) (scheduleInfo.getInterval() >> 24));
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 11] = ((byte) (scheduleInfo.getInterval() >> 16));
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 12] = ((byte) (scheduleInfo.getInterval() >> 8));
            scheduleData[i * SCHEDULE_PACKAGE_SIZE + 13] = ((byte) scheduleInfo.getInterval());
            i++;

            if (i > MAX_SCHEDULE_SIZE) {
                log.warn("[Schedule Data] Too many schedules are exist({}). It should be under {}", tagScheduleTime.size(), MAX_SCHEDULE_SIZE);
            }
        }
        String returnStr = Base64Utils.encodeToString(scheduleData);
        return ResponseEntity.ok().body(returnStr);
    }
}
