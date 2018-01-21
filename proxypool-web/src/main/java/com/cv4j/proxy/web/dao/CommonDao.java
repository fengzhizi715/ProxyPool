package com.cv4j.proxy.web.dao;

import com.cv4j.proxy.web.domain.JobLog;
import com.cv4j.proxy.web.domain.SysSequence;

public interface CommonDao {

    boolean saveJobLog(JobLog jobLog);

    SysSequence getNextSequence(String colName);
}
