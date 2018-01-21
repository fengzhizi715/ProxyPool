package com.cv4j.proxy.web.dao.impl;

import com.cv4j.proxy.web.config.Constant;
import com.cv4j.proxy.web.dao.CommonDao;
import com.cv4j.proxy.web.domain.JobLog;
import com.cv4j.proxy.web.domain.SysSequence;
import com.safframework.tony.common.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class CommonDaoImpl implements CommonDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean saveJobLog(JobLog jobLog) {
        mongoTemplate.save(jobLog, Constant.COL_NAME_JOB_LOG);

        return Preconditions.isNotBlank(jobLog.getId());
    }

    @Override
    public SysSequence getNextSequence(String colName) {
        Query query = new Query(Criteria.where("colName").is(colName));

        Update update = new Update();
        update.inc("sequence",1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        return mongoTemplate.findAndModify(query, update, options, SysSequence.class, Constant.COL_NAME_SYS_SEQUENCE);
    }
}
