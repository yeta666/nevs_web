package com.nevs.web.controller;

import com.nevs.web.model.Award;
import com.nevs.web.service.AwardService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author YETA
 * 奖励数目相关接口
 * @date 2018/09/08/19:33
 */
@RestController
@RequestMapping(value = "/award")
public class AwardController {

    @Autowired
    private AwardService awardService;

    /**
     * 修改奖励数目接口
     * @param userId
     * @param award
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(@RequestParam(value = "userId") String userId, Award award) {
        return awardService.update(userId, award);
    }

    /**
     * 查询奖励数目接口
     * @param userId
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(value = "userId") String userId) {
        return awardService.search(userId);
    }
}
