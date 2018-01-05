package com.cv4j.proxy.web.dao;

import com.cv4j.proxy.web.dto.JobLog;
import com.cv4j.proxy.web.dto.SysSequence;

public interface CommonDao {

    void saveJobLog(JobLog jobLog);

    SysSequence getNextSequence(String colName);
}
