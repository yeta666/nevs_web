package com.nevs.web.controller;

import com.nevs.web.model.Log;
import com.nevs.web.service.LogService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author YETA
 * 日志相关操作接口
 * @date 2018/09/03/18:12
 */
@RestController
@RequestMapping(value = "/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 日志查询接口
     * @param page
     * @param size
     * @param userId
     * @param startTime
     * @param endTime
     * @param log
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 @RequestParam(value = "userId") String userId,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                 Log log) {
        return logService.search(page, size, userId, startTime, endTime, log);
    }
}
