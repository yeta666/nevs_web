package com.nevs.web.controller;

import com.nevs.web.model.User;
import com.nevs.web.service.UserService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author YETA
 * 用户相关操作接口
 * @date 2018/08/26/14:36
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    //增

    /**
     * 用户新增接口
     * @param superAdminID
     * @param user
     * @return
     */
    @PostMapping(value = "/insert")
    public CommonResponse insert(@RequestParam(value = "superAdminID", required = false) String superAdminID, User user) {
        return userService.insert(superAdminID, user);
    }

    //删

    //改

    /**
     * 用户修改信息接口
     * @param userId
     * @param user
     * @param newPassword
     * @param dimission
     * @return
     */
    @PostMapping(value = "/update")
    public CommonResponse update(@RequestParam(value = "userId", required = false) String userId,
                                 User user,
                                 @RequestParam(value = "newPassword", required = false) String newPassword,
                                 @RequestParam(value = "dimission", required = false) Integer dimission) {
        return userService.update(userId, user, newPassword, dimission);
    }

    //查

    /**
     * 用户获取验证码接口
     * @param request
     * @return
     */
    @GetMapping(value = "/vCode")
    public CommonResponse vCode(HttpServletRequest request) {
        return userService.vCode(request);
    }

    /**
     * 用户获取邀请码接口
     * @param id
     * @param registerUrl
     * @return
     */
    @GetMapping(value = "/iCode")
    public CommonResponse iCode(@RequestParam(value = "id") String id,
                                @RequestParam(value = "registerUrl") String registerUrl) {
        return userService.iCode(id, registerUrl);
    }

    /**
     * 根据条件查询用户接口
     * @param page
     * @param size
     * @param user
     * @param type
     * @return
     */
    @GetMapping(value = "/search")
    public CommonResponse search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                 User user,
                                 @RequestParam(value = "type", required = false) Integer type) {
        return userService.search(page, size, user, type);
    }

    //其他

    /**
     * 用户登陆接口
     * @param user
     * @param vCode
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    public CommonResponse login(User user, @RequestParam(value = "vCode") String vCode, HttpServletRequest request) {
        return userService.login(user, vCode, request);
    }

    /**
     * 用户注销接口
     * @param id
     * @param request
     * @return
     */
    @GetMapping(value = "/logout")
    public CommonResponse logout(@RequestParam(value = "id") String id, HttpServletRequest request) {
        return userService.logout(id, request);
    }

    /**
     * 下载用户信息文件接口
     * @param userId
     * @param departmentId
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/download")
    public void download(@RequestParam(value = "userId") String userId,
                         @RequestParam(value = "departmentId", required = false) Integer departmentId,
                         HttpServletResponse response) throws IOException {
        userService.download(userId, departmentId, response);
    }
}
