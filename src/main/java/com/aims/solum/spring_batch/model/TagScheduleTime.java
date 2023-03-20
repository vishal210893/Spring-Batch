package com.aims.solum.spring_batch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TagScheduleTime {

    private int endTimeOffset;

    private int interval;

    private int displayPage = 1;


    @JsonCreator
    public TagScheduleTime(@JsonProperty(value = "endTimeOffset", required = true) int endTimeOffset,
                           @JsonProperty(value = "interval", required = true) int interval,
                           @JsonProperty(value = "displayPage", required = true) int displayPage) {
        this.endTimeOffset = endTimeOffset;
        this.interval = interval;
        this.displayPage = displayPage;
    }

}
