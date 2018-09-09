package com.nevs.web.controller;

import com.nevs.web.model.Vehicle;
import com.nevs.web.service.VehicleService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author YETA
 * 商品相关操作接口
 * @date 2018/08/26/17:20
 */
@RestController
@RequestMapping(value = "/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    //增

    /**
     * 商品新增接口
     * @param userId
     * @param vehicle
     * @return
     */
    @PostMapping(value = "/insert")
    public CommonResponse insert(@RequestParam(value = "userId") String userId, Vehicle vehicle) {
        return vehicleService.insert(userId, vehicle);
    }

    //删

    //改

    /**
     * 商品修改接口
     * @param userId
     * @param vehicle
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(@RequestParam(value = "userId") String userId, Vehicle vehicle) {
        return vehicleService.update(userId, vehicle);
    }

    //查

    /**
     * 商品查询接口
     * @param page
     * @param size
     * @param vehicle
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 Vehicle vehicle) {
        return vehicleService.search(page, size, vehicle);
    }
}
