package com.nevs.web.controller;

import com.nevs.web.model.Department;
import com.nevs.web.service.DepartmentService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author YETA
 * 部门相关操作接口
 * @date 2018/08/27/10:25
 */
@RestController
@RequestMapping(value = "/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //增

    /**
     * 部门新增接口
     * @param userId
     * @param department
     * @return
     */
    @PostMapping(value = "/insert")
    public CommonResponse insert(@RequestParam(value = "userId") String userId, Department department) {
        return departmentService.insert(userId, department);
    }

    //删

    /**
     * 部门删除接口
     * @param userId
     * @param id
     * @return
     */
    /*@DeleteMapping(value = "/delete")
    public CommonResponse delete(@RequestParam(value = "userId") String userId, @RequestParam(value = "id") Integer id) {
        return departmentService.delete(userId, id);
    }*/

    //改

    /**
     * 部门修改接口
     * @param userId
     * @param department
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(@RequestParam(value = "userId") String userId, Department department) {
        return departmentService.update(userId, department);
    }

    //查

    /**
     * 部门查询接口
     * @param department
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 Department department) {
        return departmentService.search(page, size, department);
    }
}
