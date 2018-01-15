package com.cv4j.proxy.web.domain;

import com.cv4j.proxy.web.config.Constant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Constant.COL_NAME_JOB_LOG)
@Data
public class JobLog {

    @Id
    private String id;
    private String jobName;
    private String startTime;
    private String endTime;
    private String resultDesc;
}
