package com.nevs.web.controller;

import com.nevs.web.model.Order;
import com.nevs.web.service.OrderService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author YETA
 * 订单相关操作接口
 * @date 2018/08/27/14:35
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //增

    /**
     * 订单新增接口
     * @param order
     * @return
     */
    @PostMapping(value = "/insert")
    public CommonResponse insert(Order order) {
        return orderService.insert(order);
    }

    //删

    //改

    /**
     * 订单修改接口
     * @param userId
     * @param pass
     * @param order
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(@RequestParam(value = "userId", required = false) String userId,
                                 @RequestParam(value = "pass", required = false) Integer pass,
                                 Order order) {
        return orderService.update(userId, pass, order);
    }

    //查

    /**
     * 订单查询接口
     * @param page
     * @param size
     * @param order
     * @param relation
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 Order order,
                                 @RequestParam(value = "relation", required = false) Integer relation) {
        return orderService.search(page, size, order, relation);
    }

}
