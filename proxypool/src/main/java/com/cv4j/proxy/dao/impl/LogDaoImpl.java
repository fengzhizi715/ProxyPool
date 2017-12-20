package com.cv4j.proxy.dao.impl;

import com.cv4j.proxy.config.Constant;
import com.cv4j.proxy.dao.LogDao;
import com.cv4j.proxy.domain.dto.JobLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class LogDaoImpl implements LogDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveJobLog(JobLogDTO jobLogDTO) {
        mongoTemplate.save(jobLogDTO, Constant.COL_NAME_Job_Log);
    }
}
