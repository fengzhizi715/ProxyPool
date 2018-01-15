package com.cv4j.proxy.web.controller;

import com.cv4j.proxy.web.job.ScheduleJobs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class CommonController {

    @Autowired
    private ScheduleJobs scheduleJobs;

    @RequestMapping(value="/{pagename}", method = RequestMethod.GET)
    public String load(@PathVariable String pagename) {
        log.info("load, pagename="+pagename);
        return pagename;
    }

    @RequestMapping(value="/launchjob")
    @ResponseBody
    public void startJob(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        log.info("manual startJob");
        try {
            httpServletResponse.setContentType("text/plain; charset=utf-8");
            ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
            if(scheduleJobs.getJobStatus() == ScheduleJobs.JOB_STATUS_RUNNING) {
                responseOutputStream.write("Job正在运行。。。".getBytes("utf-8"));
                responseOutputStream.flush();
                responseOutputStream.close();
            } else {
                log.info("scheduleJobs.cronJob() start by controller...");
                scheduleJobs.cronJob();
            }
        } catch (Exception e) {
            log.info("startJob exception e="+e.getMessage());
        }
    }

}