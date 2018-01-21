package com.cv4j.proxy.web.domain;


import com.cv4j.proxy.web.config.Constant;
import com.cv4j.proxy.web.domain.ProxyResource;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Constant.COL_NAME_RESOURCE_PLAN)
@Data
public class ResourcePlan {
    @Id
    private String id;
    private ProxyResource proxyResource;
    private Integer startPageNum;
    private Integer endPageNum;
    private long addTime;
    private long modTime;

}
