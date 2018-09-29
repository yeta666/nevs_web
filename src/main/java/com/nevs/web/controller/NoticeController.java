package com.nevs.web.controller;

import com.nevs.web.model.Notice;
import com.nevs.web.service.NoticeService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author YETA
 * 公告相关操作接口
 * @date 2018/08/26/16:02
 */
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    /**
     * 新增公告接口
     * @param notice
     * @param userId
     * @return
     */
    @PostMapping(value = "/insert")
    public CommonResponse insert(Notice notice, @RequestParam(value = "userId") String userId) {
        return noticeService.insert(notice, userId);
    }

    /**
     * 删除公告接口
     * @param userId
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public CommonResponse delete(@RequestParam(value = "userId") String userId,
                                 @RequestParam(value = "id") String id) {
        return noticeService.delete(userId, id);
    }

    /**
     * 修改公告接口
     * @param notice
     * @param userId
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(Notice notice, @RequestParam(value = "userId") String userId) {
        return noticeService.update(notice, userId);
    }

    /**
     * 查询公告接口
     * @param page
     * @param size
     * @param notice
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 Notice notice,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime){
        return noticeService.search(page, size, notice, startTime, endTime);
    }

}
