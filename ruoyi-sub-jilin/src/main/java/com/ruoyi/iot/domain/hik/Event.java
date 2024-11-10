package com.ruoyi.iot.domain.hik;

import lombok.Data;

@Data
public class Event {
    private int eventTypes[];
    private String eventDest;
}
