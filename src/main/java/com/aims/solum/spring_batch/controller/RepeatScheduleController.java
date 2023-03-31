package com.aims.solum.spring_batch.controller;

import com.aims.solum.spring_batch.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class RepeatScheduleController {

    @PostMapping("/repeatSchedule")
    public ResponseEntity<String> handle(@RequestBody List<TagScheduleTime> tagScheduleTime) {

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

    @PostMapping("/cameraInfo")
    public ResponseEntity<String> handle(@RequestBody CameraInfo cameraInfo) {

        final WifiConfig wifiConfig = cameraInfo.getEslInfo().getWifiConfig();

        final byte[] ssidBytes = wifiConfig.getSSID().getBytes();
        final byte[] keyTypeBytes = wifiConfig.getKeyType().getBytes();
        final byte[] passwordBytes = wifiConfig.getPassword().getBytes();
        final byte[] wifiConfigBytes = concat(ssidBytes, keyTypeBytes, passwordBytes);

        final UploadConfig uploadConfig = cameraInfo.getEslInfo().getUploadConfig();
        byte[] uploadModeByte = new byte[1];
        uploadModeByte[0] = (byte) ((short) uploadConfig.getUploadMode());
        final byte[] hostnameBytes = uploadConfig.getHostname().getBytes();
        final byte[] keyBytes = uploadConfig.getKey().getBytes();
        final byte[] secretBytes = uploadConfig.getSecret().getBytes();
        final byte[] bucketBytes = uploadConfig.getBucket().getBytes();
        final byte[] uploadConfigBytes = concat(uploadModeByte, hostnameBytes, keyBytes, secretBytes, bucketBytes);

        final CameraConfig cameraConfig = cameraInfo.getEslInfo().getCameraConfig();
        byte[] cameraConfigByte = new byte[11];
        cameraConfigByte[0] = (byte) ((short) cameraConfig.getImgSize());
        cameraConfigByte[1] = (byte) ((cameraConfig.getIso() >> 8) & 0xff);
        cameraConfigByte[2] = (byte) (cameraConfig.getIso() & 0xff);
        cameraConfigByte[3] = (byte) (cameraConfig.getExpTime() >> 24);
        cameraConfigByte[4] = (byte) (cameraConfig.getExpTime() >> 16);
        cameraConfigByte[5] = (byte) (cameraConfig.getExpTime() >> 8);
        cameraConfigByte[6] = (byte) cameraConfig.getExpTime();
        cameraConfigByte[7] = (byte) ((short) cameraConfig.getQuality());
        cameraConfigByte[8] = (byte) ((short) cameraConfig.getBrightness());
        cameraConfigByte[9] = (byte) ((short) cameraConfig.getContrast());
        cameraConfigByte[10] = (byte) ((short) cameraConfig.getObstacleDetectVal());

        final FilenameConfig filenameConfig = cameraInfo.getEslInfo().getFilenameConfig();
        final byte[] storeBytes = filenameConfig.getStore().getBytes();
        final byte[] sheldIdBytes = filenameConfig.getShelfId().getBytes();
        final byte[] linkedShelfIdBytes = filenameConfig.getLinkedShelfID().getBytes();
        byte[] cameraindexByte = new byte[2];
        cameraindexByte[0] = (byte) ((filenameConfig.getCameraIndex() >> 8) & 0xff);
        cameraindexByte[1] = (byte) (filenameConfig.getCameraIndex() & 0xff);
        final byte[] filenameConfigBytes = concat(storeBytes, sheldIdBytes, linkedShelfIdBytes, cameraindexByte);

        String returnStr = Base64Utils.encodeToString(concat(wifiConfigBytes, uploadConfigBytes, cameraConfigByte, filenameConfigBytes));
        return ResponseEntity.ok().body(returnStr);
    }

    public static byte[] concat(byte[]... arrays) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (arrays != null) {
            Arrays.stream(arrays)
                    .filter(Objects::nonNull)
                    .forEach(array -> out.write(array, 0, array.length));
        }
        return out.toByteArray();
    }

}
