package com.cv4j.proxy.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobLogDTO {

    private String jobName;
    private String startTime;
    private String endTime;
    private String resultDesc;
    private List<String> ipList;
}
