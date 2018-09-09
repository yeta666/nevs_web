package com.nevs.web.controller;

import com.nevs.web.model.IntegralTrading;
import com.nevs.web.service.IntegralService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author YETA
 * 积分相关操作接口
 * @date 2018/08/28/11:36
 */
@RestController
@RequestMapping(value = "/integral")
public class IntegralController {

    @Autowired
    private IntegralService integralService;

    /**
     * 积分提现接口
     * @param userId
     * @param amount
     * @return
     */
    @PostMapping(value = "/insert")
    public CommonResponse insert(@RequestParam(value = "userId") String userId,
                               @RequestParam(value = "amount") Integer amount) {
        return integralService.insert(userId, amount);
    }

    /**
     * 审核提现接口
     * @param userId
     * @param id
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(@RequestParam(value = "userId") String userId,
                                 @RequestParam(value = "id") String id) {
        return integralService.update(userId, id);
    }

    /**
     * 积分查询接口
     * @param userId
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(name = "userId") String userId) {
        return integralService.search(userId);
    }

    /**
     * 积分交易查询接口
     * @param page
     * @param size
     * @param integralTrading
     * @return
     */
    @GetMapping(value = "/trading/search")
    public CommonResponse searchTrading(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                                        IntegralTrading integralTrading) {
        return integralService.searchTrading(page, size, integralTrading);
    }
}
