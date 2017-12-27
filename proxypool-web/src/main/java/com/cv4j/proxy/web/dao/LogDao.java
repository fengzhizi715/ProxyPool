package com.cv4j.proxy.web.dao;

import com.cv4j.proxy.domain.dto.JobLogDTO;

public interface LogDao {

    void saveJobLog(JobLogDTO jobLogDTO);
}
