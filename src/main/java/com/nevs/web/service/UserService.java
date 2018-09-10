package com.nevs.web.service;

import com.nevs.web.exception.UserException;
import com.nevs.web.model.Department;
import com.nevs.web.model.User;
import com.nevs.web.repository.DepartmentRepository;
import com.nevs.web.repository.IntegralTradingRepository;
import com.nevs.web.repository.UserRepository;
import com.nevs.web.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @author YETA
 * 用户相关操作逻辑处理
 * @date 2018/08/26/14:19
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CommonUtil commonUtil;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    /**
     * 用户注册
     * @param superAdminID
     * @param user
     * @return
     */
    public CommonResponse insert(String superAdminID, User user) {
        //判断参数
        String username = user.getUsername();
        String name = user.getName();
        if (commonUtil.isNull(user.getUsername()) || commonUtil.isNull(user.getPassword()) || commonUtil.isNull(user.getInvitationCode()) || commonUtil.isNull(name)) {
            return new CommonResponse(false, 3, "用户名、密码、邀请码、姓名不能为空");
        }
        //判断用户名是否存在
        if (userRepository.findByUsername(username) != null) {
            return new CommonResponse(false, 3, "用户名已存在");
        }
        //判断姓名是否存在
        if (userRepository.findByName(name) != null) {
            return new CommonResponse(false, 3, "姓名已存在，如果存在重名，请在名字后面添加数字");
        }
        //判断是新增管理员还是新增用户
        if (superAdminID != null) {        //新增管理员
            return insertAdmin(superAdminID, user);
        } else {        //新增用户
            return insertUser(user);
        }
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    public CommonResponse insertUser(User user) {
        //判断邀请码
        Optional<User> userOptional = userRepository.findById(user.getInvitationCode());
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "邀请码错误");
        }
        User iUser = userOptional.get();
        //判断邀请人角色
        Integer iRoleId = iUser.getRoleId();
        if (iRoleId > 3) {     //角色大于3的用户的邀请码都不能使用
            return new CommonResponse(false, 3, "邀请码错误");
        } else  if (iUser.getRoleId() == 1) {       //1:超级管理员
            user.setDepartmentId(0);
        } else {
            user.setDepartmentId(iUser.getDepartmentId());
        }
        //补全用户信息
        user.setId(UUID.randomUUID().toString());
        user.setInviter(iUser.getName());
        user.setInvitationCodeOfInviter(iUser.getInvitationCode());
        user.setInviterOfInviter(userRepository.findById(iUser.getInvitationCode()).get().getName());
        user.setRoleId(3);
        user.setIntegral(0);
        user.setTotalSales(0);
        user.setIndirectSales(0);
        //保存
        if (userRepository.save(user) == null) {
            return new CommonResponse(false, 3, "注册失败");
        }
        return new CommonResponse();
    }

    /**
     * 新增管理员
     * @param superAdminID
     * @param user
     * @return
     */
    public CommonResponse insertAdmin(String superAdminID, User user) {
        //判断当前用户是否是超级管理员
        Optional<User> userOptional = userRepository.findById(superAdminID);
        if (!userOptional.isPresent() || userOptional.get().getRoleId() != 1) {
            return new CommonResponse(false, 3, "无权新增管理员");
        }
        //补全管理员信息
        user.setId(UUID.randomUUID().toString());
        user.setRoleId(2);
        //保存
        if (userRepository.save(user) == null) {
            return new CommonResponse(false, 3, "新增管理员失败");
        }
        return new CommonResponse();
    }

    /**
     * 用户登陆
     * @param loginUser
     * @param vCode
     * @param request
     * @return
     */
    public CommonResponse login(User loginUser, String vCode, HttpServletRequest request) {
        //判断参数
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (commonUtil.isNull(username) || commonUtil.isNull(password)) {
            return new CommonResponse(false, 3, "用户名、密码不能为空");
        }
        //判断验证码
        HttpSession session = request.getSession();
        Object svCode = session.getAttribute("vCode");
        if (svCode == null || !svCode.equals(vCode.toUpperCase())) {
            return new CommonResponse(false, 3, "验证码错误");
        }
        //判断用户名
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new CommonResponse(false, 3, "用户名不存在");
        }
        String vid = user.getId();
        //判断是否已登录
        ServletContext servletContext = session.getServletContext();
        List<String> onlines = (List<String>) servletContext.getAttribute("onlines");
        for (String online: onlines) {
            if (online.equals(vid)) {
                //重复登陆
                return new CommonResponse(false, 3, "重复登陆");
            }
        }
        //判断密码
        String vPassword = user.getPassword();
        if (vPassword == null || !vPassword.equals(password)) {
            return new CommonResponse(false, 3, "密码错误");
        }
        //超级管理员记录行为日志
        if (user.getRoleId() == 1) {        //1:超级管理员
            commonUtil.addLog(1, null, user.getName());
        }
        //判断部门
        Integer departmentId = user.getDepartmentId();
        if (commonUtil.isNull(departmentId) || departmentId  == 0) {        //超级管理员邀请的人
            return new CommonResponse(false, 3, "请加入部门后再登陆");
        }
        //判断角色
        Integer roleId = user.getRoleId();
        if (commonUtil.isNull(roleId) || roleId == 7) {     //已离职
            return new CommonResponse(false, 3, "您已离职，无法登陆");
        }
        //设置已登录
        onlines.add(vid);
        session.setAttribute("id", vid);
        session.setMaxInactiveInterval(60 * 60);      //60分钟
        //设置返回值
        Map<String, String> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("token", MD5Util.getMd5(user.getId()));
        LOG.info("当前在线人数...{}...", onlines.size());
        LOG.info(Arrays.toString(onlines.toArray()));
        return new CommonResponse(result);
    }

    @Value("${nevs.download}")
    private String download;

    /**
     * 获取验证码
     * @param request
     * @return
     */
    public CommonResponse vCode(HttpServletRequest request) {
        int w = 24, h = 52, size = 5;
        String code = VerifyCodeUtil.generateVerifyCode(size).toUpperCase();
        request.getSession().setAttribute("vCode", code);
        File file = new File(new File(download), System.currentTimeMillis() + "_" + new Random().nextInt() + ".jpg");
        try {
            VerifyCodeUtil.outputImage(w * code.length(), h, file, code);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UserException("获取验证码失败");
        }
        return new CommonResponse("/download/" + file.getName());
    }

    /**
     * 用户注销
     * @param id
     * @param request
     * @return
     */
    public CommonResponse logout(String id, HttpServletRequest request) {
        //判断用户是否存在
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "用户不存在");
        }
        //超级管理员行为记录日志
        if (userOptional.get().getRoleId() == 1) {      //1:超级管理员
            commonUtil.addLog(2, null, userOptional.get().getName());
        }
        //获取session
        HttpSession httpSession = request.getSession();
        //获取application
        ServletContext servletContext = httpSession.getServletContext();
        //获取在线用户列表
        List<String> onlines = (List<String>) servletContext.getAttribute("onlines");
        boolean flag = false;
        for (String online : onlines) {
            if (id.equals(online)) {
                flag = true;
                onlines.remove(online);
                LOG.info("当前在线人数...{}...", onlines.size());
                LOG.info(Arrays.toString(onlines.toArray()));
                break;
            }
        }
        httpSession.invalidate();
        if (flag) {
            return new CommonResponse();
        } else {
            return new CommonResponse(false, 3, "注销失败");
        }
    }

    /**
     * 用户修改信息
     * @param userId
     * @param updateUser
     * @param newPassword
     * @param dimission
     * @return
     */
    public CommonResponse update(String userId, User updateUser, String newPassword, Integer dimission) {
        //判断参数
        String id = updateUser.getId();
        if (commonUtil.isNull(id)) {
            return new CommonResponse(false, 3, "用户id不能为空");
        }
        //判断当前用户是否存在
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "用户不存在");
        }
        //判断修改密码还是修改用户信息
        if (!commonUtil.isNull(newPassword) && commonUtil.isNull(userId)) {      //修改密码
            return updatePassword(updateUser, userOptional.get(), newPassword);
        } else if (!commonUtil.isNull(userId) && commonUtil.isNull(newPassword)) {        //修改用户信息
            return updateUser(userId, updateUser, dimission);
        } else {
            return new CommonResponse(false, 3, "修改用户信息失败");
        }
    }

    /**
     * 修改密码
     * @param updateUser
     * @param user
     * @param newPassword
     * @return
     */
    public CommonResponse updatePassword(User updateUser, User user, String newPassword) {
        //判断用户名
        if (commonUtil.isNull(updateUser.getUsername()) ||
                commonUtil.isNull(user.getUsername()) ||
                !updateUser.getUsername().equals(user.getUsername())) {
            return new CommonResponse(false, 3, "用户名错误");
        }
        //判断原密码
        if (commonUtil.isNull(updateUser.getPassword()) ||
                commonUtil.isNull(updateUser.getPassword()) ||
                !updateUser.getPassword().equals(user.getPassword())) {
            return new CommonResponse(false, 3, "原密码错误");
        }
        //修改密码
        if (userRepository.updatePassword(newPassword, updateUser.getId()) != 1) {
            return new CommonResponse(false, 3, "修改密码失败");
        }
        return new CommonResponse();
    }

    /**
     * 修改用户信息
     * @param userId
     * @param user
     * @param dimission
     * @return
     */
    public CommonResponse updateUser(String userId, User user, Integer dimission) {
        //判断权限
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User admin = userOptional.get();
            if (admin.getRoleId() < 3) {        //只有超级管理员和部门管理员可以修改信息
                //修改
                if (!commonUtil.isNull(dimission) && dimission == 1) {
                    if (userRepository.updateUser(user.getIdCardNo(),
                            user.getImageUrl1(),
                            user.getImageUrl2(),
                            user.getCreditCardNo(),
                            user.getBankOfDeposit(),
                            user.getPhone(),
                            7,
                            user.getId()) == 1) {
                        return new CommonResponse();
                    }
                } else {
                    if (userRepository.updateUser(user.getIdCardNo(),
                            user.getImageUrl1(),
                            user.getImageUrl2(),
                            user.getCreditCardNo(),
                            user.getBankOfDeposit(),
                            user.getPhone(),
                            user.getRoleId(),
                            user.getId()) == 1) {
                        return new CommonResponse();
                    }
                }
                //超级管理员记录行为日志
                if (admin.getRoleId() == 1) {
                    commonUtil.addLog(6, "修改姓名为：" + user.getName() + "的用户信息", admin.getName());
                }
            }
        }
        return new CommonResponse(false, 3, "修改信息失败");
    }

    @Autowired
    private QRCodeUtil qrCodeUtil;

    /**
     * 用户获取自己的邀请码
     * @param id
     * @param registerUrl
     * @return
     */
    public CommonResponse iCode(String id, String registerUrl) {
        //查找用户
        Optional<User> userOption = userRepository.findById(id);
        //判断用户是否存在，判断用户是否存在部门
        Integer departmentId = userOption.get().getDepartmentId();
        if (userOption.isPresent() && departmentId > 0 && departmentId < 4) {        //只有1:超级管理员、2:部门管理员、3:销售才能查看邀请码
            String path = qrCodeUtil.getQRCode(registerUrl + "?iCode=" + id, 300, 300);
            Map<String, String> result = new HashMap<>();
            result.put("iCode", id);
            result.put("iCodeUrl", path);
            return new CommonResponse(result);
        }
        return new CommonResponse(false, 3, "获取邀请码失败");
    }

    /**
     * 根据条件查询用户
     * @param page
     * @param size
     * @param user
     * @param type
     * @return
     */
    public CommonResponse search(Integer page, Integer size, User user, Integer type) {
        //判断type
        if (type != null) {
            if (type == 1) {        //增加部门时筛选用户
                return new CommonResponse(userRepository.findAllByDepartmentId(0));
            } else if (type == 2) {     //修改部门时筛选用户
                List<User> userList = userRepository.findAllByDepartmentId(0);
                userList.addAll(userRepository.findAllByDepartmentIdAndRoleIdNotIn(user.getDepartmentId(), 7));     //7:已离职
                return new CommonResponse(userList);
            }
            return new CommonResponse(false, 3, "获取用户信息失败");
        } else {
            Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "totalSales"));
            Page<User> userPage = userRepository.findAll(new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<Predicate>();
                    //筛选id
                    String id = user.getId();
                    if (!commonUtil.isNull(id)) {
                        predicateList.add(criteriaBuilder.equal(root.get("id").as(String.class), id));
                    }
                    //筛选name
                    String name = user.getName();
                    if (!commonUtil.isNull(name)) {
                        predicateList.add(criteriaBuilder.equal(root.get("name").as(String.class), name));
                    }
                    //筛选departmentId
                    Integer departmentId = user.getDepartmentId();
                    if (!commonUtil.isNull(departmentId)) {
                        predicateList.add(criteriaBuilder.equal(root.get("departmentId").as(Integer.class), departmentId));
                    }
                    //筛选roleId
                    Integer roleId = user.getRoleId();
                    if (!commonUtil.isNull(roleId)) {
                        predicateList.add(criteriaBuilder.equal(root.get("roleId").as(Integer.class), roleId));
                    }
                    Predicate[] predicates = new Predicate[predicateList.size()];
                    return criteriaBuilder.and(predicateList.toArray(predicates));
                }
            }, pageable);
            return new CommonResponse(userPage);
        }
    }

    /**
     * 下载用户信息文件
     * @param userId
     * @param departmentId
     * @param response
     * @throws IOException
     */
    public void download(String userId, Integer departmentId, HttpServletResponse response) throws IOException {

        //验证权限
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UserException("无权下载用户信息");
        }
        Integer roleId = userOptional.get().getRoleId();
        if (roleId != 1 && roleId != 6) {
            throw new UserException("无权下载用户信息");
        }

        //创建Excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //创建一个工作表sheet
        HSSFSheet sheet = workbook.createSheet();

        //创建标题行
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("姓名");
        titleRow.createCell(1).setCellValue("电话");
        titleRow.createCell(2).setCellValue("邀请人");
        titleRow.createCell(3).setCellValue("部门名");
        titleRow.createCell(4).setCellValue("总销售量");
        titleRow.createCell(5).setCellValue("间接销售量");
        titleRow.createCell(6).setCellValue("积分");

        if (!commonUtil.isNull(departmentId)) {
            //根据部门id查询部门
            if (departmentId > 1) {
                Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
                if (departmentOptional.isPresent()) {
                    //根据部门id查询用户
                    Department department = departmentOptional.get();
                    List<User> userList = userRepository.findAllByDepartmentId(department.getId());
                    for (int i = 1; i <= userList.size(); i++) {
                        User user = userList.get(i - 1);
                        HSSFRow row = sheet.createRow(i);
                        row.createCell(0).setCellValue(user.getName());
                        row.createCell(1).setCellValue(user.getPhone());
                        row.createCell(2).setCellValue(user.getInviter());
                        row.createCell(3).setCellValue(department.getName());
                        row.createCell(4).setCellValue(user.getTotalSales());
                        row.createCell(5).setCellValue(user.getIndirectSales());
                        if (user.getRoleId() == 7) {
                            row.createCell(6).setCellValue(0);
                            row.createCell(7).setCellValue("已离职");
                        } else {
                            row.createCell(6).setCellValue(user.getIntegral());
                        }
                    }
                    //超级管理员记录行为日志
                    commonUtil.addLog(7, "下载部门名：" + department.getName() + "的用户信息", userOptional.get().getName());
                }
            }
        } else {
            //查询所有部门
            List<Department> departmentList = departmentRepository.findAllByIdGreaterThan(1);
            int rowNum = 1;
            for (Department department : departmentList) {
                //根据部门id查询用户
                List<User> userList = userRepository.findAllByDepartmentId(department.getId());
                for (User user : userList) {
                    HSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(user.getName());
                    row.createCell(1).setCellValue(user.getPhone());
                    row.createCell(2).setCellValue(user.getInviter());
                    row.createCell(3).setCellValue(department.getName());
                    row.createCell(4).setCellValue(user.getTotalSales());
                    row.createCell(5).setCellValue(user.getIndirectSales());
                    if (user.getRoleId() == 7) {
                        row.createCell(6).setCellValue(0);
                        row.createCell(7).setCellValue("已离职");
                    } else {
                        row.createCell(6).setCellValue(user.getIntegral());
                    }
                }
            }
            //超级管理员记录行为日志
            commonUtil.addLog(7, "下载所有部门的用户信息", userOptional.get().getName());
        }

        //强制下载
        response.setContentType("application/force-download");
        //设置下载文件的文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + System.currentTimeMillis() + ".xls");
        //初始化响应输出流
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);

        //清理
        outputStream.close();
        workbook.close();
    }
}
