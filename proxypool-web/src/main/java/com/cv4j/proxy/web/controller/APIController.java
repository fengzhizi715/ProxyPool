package com.cv4j.proxy.web.controller;

import com.cv4j.proxy.web.aop.annotation.WebLog;
import com.cv4j.proxy.web.dao.ProxyDao;
import com.cv4j.proxy.web.dto.ResultProxyDataDTO;
import com.cv4j.proxy.web.dto.ProxyDataDTO;
import com.safframework.tony.common.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class APIController {

    @Autowired
    private ProxyDao proxyDao;

    @WebLog
    @Cacheable(value="proxys")
    @RequestMapping(value="/proxys/{count}", method = RequestMethod.GET)
    public ResultProxyDataDTO getProxyData(@PathVariable String count) {

        int code = 0;
        String message = "";
        List<ProxyDataDTO> data = new ArrayList<>();
        if(isNumeric(count)) {
            data = proxyDao.findLimitProxy(Integer.parseInt(count));

            if(Preconditions.isNotBlank(data)) {
                code = 200;
                message = "成功返回有效数据";
            } else {
                code = 404;
                message = "无法返回有效数据";
            }
        } else {
            code = 400;
            message = "参数格式无效，请输入数字";
        }

        ResultProxyDataDTO resultProxyDataDTO = new ResultProxyDataDTO();
        resultProxyDataDTO.setCode(code);
        resultProxyDataDTO.setMessage(message);
        resultProxyDataDTO.setData(data);

        return resultProxyDataDTO;
    }

    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}