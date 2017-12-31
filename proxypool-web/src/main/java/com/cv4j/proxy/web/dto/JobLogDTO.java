package com.cv4j.proxy.web.dto;

import lombok.Data;

@Data
public class JobLogDTO {

    private String jobName;
    private String startTime;
    private String endTime;
    private String resultDesc;
}
