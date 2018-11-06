package com.nevs.web.service;

import com.nevs.web.exception.UserException;
import com.nevs.web.model.Award;
import com.nevs.web.model.Department;
import com.nevs.web.model.IntegralTrading;
import com.nevs.web.model.User;
import com.nevs.web.repository.AwardRepository;
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
import org.springframework.transaction.annotation.Transactional;

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
    private AwardRepository awardRepository;

    @Autowired
    private IntegralTradingRepository integralTradingRepository;

    @Autowired
    private CommonUtil commonUtil;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Transactional
    public CommonResponse insert(User user) {

        //判断参数
        String username = user.getUsername();
        if (commonUtil.isNull(username) || commonUtil.isNull(user.getPassword()) || commonUtil.isNull(user.getInvitationCode()) || commonUtil.isNull(user.getName())) {
            return new CommonResponse(false, 3, "用户名、密码、邀请码、姓名不能为空");
        }

        //判断用户名是否存在
        if (userRepository.findByUsername(username) != null) {
            return new CommonResponse(false, 3, "用户名已存在");
        }

        //判断邀请码
        Optional<User> userOptional = userRepository.findById(user.getInvitationCode());
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "邀请码错误");
        }
        User iUser = userOptional.get();
        Integer iRoleId = iUser.getRoleId();
        if (iRoleId != 1 && iRoleId != 2 && iRoleId != 3 && iRoleId != 8) {
            return new CommonResponse(false, 3, "邀请码错误");
        }

        //判断邀请人部门
        Integer departmentId = iUser.getDepartmentId();
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
        if (!departmentOptional.isPresent()) {
            return new CommonResponse(false, 3, "邀请人部门不存在");
        }
        Department department = departmentOptional.get();

        //补全用户信息
        user.setId(UUID.randomUUID().toString());
        user.setInviter(iUser.getName());       //邀请人的姓名
        user.setInvitationCodeOfInviter(iUser.getInvitationCode());     //邀请人的邀请人的邀请码
        user.setInviterOfInviter(iUser.getInviter());       //邀请人的邀请人的姓名
        user.setRoleId(3);
        user.setDepartmentId(departmentId);
        user.setIntegral(0);
        user.setCarIntegral(0);
        user.setTotalSales(0);
        user.setIndirectSales(0);
        if (iRoleId == 1) {       //1:超级管理员
            //设置部门id为0，表示未加入部门
            user.setDepartmentId(0);
        } else if (iRoleId == 8) {      //8:股东
            if (iUser.getDepartmentId() != 1) {     //非给定的股东账号
                user.setRoleId(8);
                //邀请人增加积分
                List<Award> awardList = awardRepository.findAll();
                if (awardList == null || awardList.size() != 1) {
                    return new CommonResponse(false, 3, "股东邀请人增加积分失败");
                }
                Award award = awardList.get(0);
                Integer shareholderReward = award.getShareholderReward();
                Integer shareholderCarReward = award.getShareholderCarReward();
                if (shareholderReward == null || shareholderCarReward == null) {
                    return new CommonResponse(false, 3, "股东邀请人增加积分失败");
                }
                iUser.setIntegral(iUser.getIntegral() + shareholderReward);
                iUser.setCarIntegral(iUser.getCarIntegral() + shareholderCarReward);
                if (userRepository.save(iUser) == null) {
                    return new CommonResponse(false, 3, "股东邀请人增加积分失败");
                }
                //记录积分交易
                if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                        iUser.getId(),
                        iUser.getName(),
                        departmentId,
                        department.getName(),
                        1,
                        shareholderReward,
                        "股东邀请股东，奖励可提现积分",
                        new Date())) == null) {
                    throw new UserException("股东邀请人增加积分失败");
                }
                //记录积分交易
                if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                        iUser.getId(),
                        iUser.getName(),
                        departmentId,
                        department.getName(),
                        1,
                        shareholderCarReward,
                        "股东邀请股东，奖励购车积分",
                        new Date())) == null) {
                    throw new UserException("股东邀请人增加积分失败");
                }
            }
        }

        //注册
        if (userRepository.save(user) == null) {
            throw new UserException("注册失败");
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
     * @param user
     * @param newPassword
     * @param dimission
     * @return
     */
    public CommonResponse update(String userId, User user, String newPassword, Integer dimission) {
        //判断修改操作
        if (!commonUtil.isNull(newPassword)) {      //修改密码
            return updatePassword(user, newPassword);
        }
        if (dimission != null) {        //设置离/复职
            return updateDimission(userId, user, dimission);
        }
        if (newPassword == null && dimission == null && !commonUtil.isNull(userId)) {       //修改用户信息
            return updateUser(userId, user);
        }
        return new CommonResponse(false, 3, "修改失败");
    }

    /**
     * 修改密码
     * @param user
     * @param newPassword
     * @return
     */
    public CommonResponse updatePassword(User user, String newPassword) {
        //判断参数
        String id = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        if (commonUtil.isNull(id) ||
                commonUtil.isNull(username) ||
                commonUtil.isNull(password)) {
            return new CommonResponse(false, 3, "参数不匹配");
        }
        Optional<User> sUserOptional = userRepository.findById(id);
        if (!sUserOptional.isPresent()) {
            return new CommonResponse(false, 3, "用户不存在");
        }
        User sUser = sUserOptional.get();
        //验证用户名、原密码是否匹配
        if (!username.equals(sUser.getUsername()) || !password.equals(sUser.getPassword())) {
            return new CommonResponse(false, 3, "用户名或原密码不正确");
        }
        //修改密码
        if (userRepository.updatePassword(newPassword, id) != 1) {
            return new CommonResponse(false, 3, "修改密码失败");
        }
        return new CommonResponse();
    }

    /**
     * 修改用户信息
     * @param userId
     * @param user
     * @return
     */
    public CommonResponse updateUser(String userId, User user) {
        //判断参数
        if (commonUtil.isNull(user.getId()) ||
                commonUtil.isNull(user.getIdCardNo()) ||
                commonUtil.isNull(user.getImageUrl1()) ||
                commonUtil.isNull(user.getImageUrl2()) ||
                commonUtil.isNull(user.getCreditCardNo()) ||
                commonUtil.isNull(user.getBankOfDeposit()) ||
                commonUtil.isNull(user.getPhone())) {
            return new CommonResponse(false, 3, "参数不匹配");
        }
        //判断权限
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "无权修改用户信息");
        }
        User admin = userOptional.get();
        if (admin.getRoleId() != 1 && admin.getRoleId() != 2) {       //不是超级管理员或部门管理员
            return new CommonResponse(false, 3, "无权修改用户信息");
        }
        //修改用户信息
        if (userRepository.updateUser(user.getIdCardNo(),
                user.getImageUrl1(),
                user.getImageUrl2(),
                user.getCreditCardNo(),
                user.getBankOfDeposit(),
                user.getPhone(),
                user.getId()) != 1) {
            return new CommonResponse(false, 3, "修改用户信息失败");
        }
        //超级管理员记录行为日志
        if (!commonUtil.addLog(6, "修改姓名为：" + user.getName() + "的用户信息", admin.getName())) {
            throw new UserException("保存日志失败");
        }
        return new CommonResponse();
    }

    /**
     * 设置离/复职
     * @param userId
     * @param user
     * @param dimission
     * @return
     */
    public CommonResponse updateDimission(String userId, User user, Integer dimission) {
        //判断权限
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "无权设置离/复职");
        }
        User admin = userOptional.get();
        if (admin.getRoleId() != 1) {       //不是超级管理员
            return new CommonResponse(false, 3, "无权设置离/复职");
        }
        //判断参数
        String id = user.getId();
        if (commonUtil.isNull(id)) {
            return new CommonResponse(false, 3, "参数不匹配");
        }
        //获取被修改用户
        Optional<User> userOptional1 = userRepository.findById(id);
        if (!userOptional1.isPresent()) {
            return new CommonResponse(false, 3, "用户不存在");
        }
        User user1 = userOptional1.get();
        //判断离职还是复职
        if (dimission == 1) {       //离职
            //判断设置离职的用户是否销售
            if (user1.getRoleId() != 3 && user1.getRoleId() != 8) {
                return new CommonResponse(false, 3, "只有销售和股东用户才能被设置离职");
            }
            //设置离职
            if (userRepository.updateRoleId(7, id) != 1) {
                return new CommonResponse(false, 3, "设置离职失败");
            }
            //超级管理员记录行为日志
            if (!commonUtil.addLog(6, "设置姓名为：" + user1.getName() + "的用户离职", admin.getName())) {
                throw new UserException("保存日志失败");
            }
            return new CommonResponse();
        } else if (dimission == 2) {        //复职
            //判断设置复职的用户是否已离职
            if (user1.getRoleId() != 7) {
                return new CommonResponse(false, 3, "只有已离职的用户才能设置复职");
            }
            int roleId;
            if (user1.getDepartmentId() == 2) {
                roleId = 8;
            } else {
                roleId = 3;
            }
            //设置复职
            if (userRepository.updateRoleId(roleId, id) != 1) {
                return new CommonResponse(false, 3, "设置复职失败");
            }
            //超级管理员记录行为日志
            if (!commonUtil.addLog(6, "设置姓名为：" + user1.getName() + "的用户复职", admin.getName())) {
                throw new UserException("保存日志失败");
            }
            return new CommonResponse();
        } else {
            return new CommonResponse(false, 3, "参数不匹配");
        }
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
        if (userOption.isPresent()) {
            //判断权限
            Integer roleId = userOption.get().getRoleId();
            if (roleId == 1 || roleId == 2 || roleId == 3 || roleId == 8) {        //只有1:超级管理员、2:部门管理员、3:销售、8：股东才能查看邀请码
                String path = qrCodeUtil.getQRCode(registerUrl + "?iCode=" + id, 300, 300);
                Map<String, String> result = new HashMap<>();
                result.put("iCode", id);
                result.put("iCodeUrl", path);
                return new CommonResponse(result);
            }
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
                            if (user.getRoleId() == 8) {
                                row.createCell(8).setCellValue("股东，购车积分：" + user.getCarIntegral());
                            }
                        }
                    }
                    //超级管理员记录行为日志
                    if (roleId == 1) {
                        commonUtil.addLog(7, "下载部门名：" + department.getName() + "的用户信息", userOptional.get().getName());
                    }
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
                    } else if (user.getRoleId() == 8) {
                        row.createCell(6).setCellValue(0);
                        row.createCell(7).setCellValue("已离职");
                    } else {
                        row.createCell(6).setCellValue(user.getIntegral());
                    }
                }
            }
            //超级管理员记录行为日志
            if (roleId == 1) {
                commonUtil.addLog(7, "下载所有部门的用户信息", userOptional.get().getName());
            }
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
