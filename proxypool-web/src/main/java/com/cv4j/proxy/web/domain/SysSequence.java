package com.cv4j.proxy.web.domain;

import com.cv4j.proxy.web.config.Constant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Constant.COL_NAME_SYS_SEQUENCE)
@Data
public class SysSequence {
    private String colName;
    private Integer sequence;
}
