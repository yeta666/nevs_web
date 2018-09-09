package com.nevs.web.controller;


import com.nevs.web.model.System;
import com.nevs.web.service.SystemService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author YETA
 * 系统信息相关操作接口
 * @date 2018/08/26/16:02
 */
@RestController
@RequestMapping(value = "/system")
public class SystemController {

    @Autowired
    private SystemService systemService;

    /**
     * 系统信息修改接口
     * @param system
     * @param userId
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(System system, @RequestParam("userId") String userId) {
        return systemService.update(system, userId);
    }

    /**
     * 系统信息查询接口
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search() {
        return systemService.search();
    }

}
