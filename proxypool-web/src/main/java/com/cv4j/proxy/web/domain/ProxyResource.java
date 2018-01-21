package com.cv4j.proxy.web.domain;


import com.cv4j.proxy.web.config.Constant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Constant.COL_NAME_PROXY_RESOURCE)
@Data
public class ProxyResource {
    @Id
    private String id;
    private Integer resId;
    private String webName;
    private String webUrl;
    private Integer pageCount;
    private String prefix;
    private String suffix;
    private String parser;
    private long addTime;
    private long modTime;
}
