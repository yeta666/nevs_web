package com.nevs.web.service;

import com.nevs.web.exception.DepartmentException;
import com.nevs.web.exception.OrderException;
import com.nevs.web.model.*;
import com.nevs.web.repository.*;
import com.nevs.web.util.CommonResponse;
import com.nevs.web.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author YETA
 * 部门相关操作逻辑处理
 * @date 2018/08/26/16:02
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IntegralTradingRepository integralTradingRepository;

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 新增部门
     * @param userId
     * @param department
     * @return
     */
    @Transactional
    public CommonResponse insert(String userId, Department department) {
        //判断权限
        Optional<User> superAdmin = userRepository.findById(userId);
        if (!superAdmin.isPresent() || superAdmin.get().getRoleId() != 1) {     //1:超级管理员
            return new CommonResponse(false, 3, "无权创建部门");
        }
        //判断参数
        String name = department.getName();
        String managerId = department.getManagerId();
        String managerName = department.getManagerName();
        if (commonUtil.isNull(name) || commonUtil.isNull(managerId) || commonUtil.isNull(managerName)) {
            return new CommonResponse(false, 3, "部门名、部门管理id、部门管理员姓名不能为空");
        }
        //判断部门名
        if (departmentRepository.findByName(name) != null) {
            return new CommonResponse(false, 3, "部门名已存在");
        }
        //判断部门管理员
        Optional<User> userOptional1 = userRepository.findById(managerId);
        if (!userOptional1.isPresent() || userOptional1.get().getRoleId() != 3) {       //只有销售能成为部门管理员
            return new CommonResponse(false, 3, "选择的用户不能成为部门管理员");
        }
        //保存部门
        department.setCreateTime(new Date());
        department.setTotalSales(0);
        department.setQuarterlySales(0);
        department.setLastLiquidationTime(department.getCreateTime());
        department.setFlag(0);
        Department savedDepartment = departmentRepository.save(department);
        if (savedDepartment == null) {
            throw new DepartmentException("保存部门失败");
        }
        //设置部门管理员
        if (userRepository.updateDepartmentIdAndRoleId(savedDepartment.getId(), 2, managerId) != 1) {       //2:部门管理员
            throw new DepartmentException("设置部门管理员失败");
        }
        //超级管理员记录行为日志
        commonUtil.addLog(3, "部门名：" + name + "，部门管理员：" + managerName, superAdmin.get().getName());
        return new CommonResponse();
    }

    /**
     * 删除部门
     * @param userId
     * @param id
     * @return
     */
    public CommonResponse delete(String userId, Integer id) {
        //判断权限
        if (!commonUtil.verifyAuthority(userId, 1)) {       //1:超级管理员
            return new CommonResponse(false, 3, "无权删除部门");
        }
        if (departmentRepository.deleteDepartment(id) == 1) {
            return new CommonResponse();
        }
        return new CommonResponse(false, 3, "删除部门失败");
    }

    /**
     * 修改部门
     * @param userId
     * @param department
     * @return
     */
    @Transactional
    public CommonResponse update(String userId, Department department) {
        //判断权限
        Optional<User> superAdmin = userRepository.findById(userId);
        if (!superAdmin.isPresent() || superAdmin.get().getRoleId() != 1) {     //1:超级管理员
            return new CommonResponse(false, 3, "无权创建部门");
        }
        //判断参数
        Integer id = department.getId();
        String name = department.getName();
        String managerId = department.getManagerId();
        String managerName = department.getManagerName();
        if (commonUtil.isNull(id) || commonUtil.isNull(name) || commonUtil.isNull(managerId) || commonUtil.isNull(managerName)) {
            return new CommonResponse(false, 3, "部门id、部门名、部门管理员id、部门管理员姓名不能为空");
        }
        //判断部门
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (!departmentOptional.isPresent()) {
            return new CommonResponse(false, 3, "部门不存在");
        }
        String savedName = departmentOptional.get().getName();
        String savedManagerId = departmentOptional.get().getManagerId();
        //判断部门管理员是否修改
        if (savedManagerId.equals(managerId)) {     //未修改部门管理员
            //判断部门名是否修改
            if (savedName.equals(name)) {       //未修改部门名
                return new CommonResponse(false, 3, "未修改任何信息");
            } else {        //修改部门名
                if (departmentRepository.updateDepartment(name, managerId, managerName, id) != 1) {
                    throw new DepartmentException("修改部门名失败");
                }
                //超级管理员记录行为日志
                commonUtil.addLog(4, "原部门名：" + savedName + "，现部门名：" + name, superAdmin.get().getName());
            }

        } else {        //修改部门管理员
            //判断新部门管理员是否存在
            Optional<User> userOptional = userRepository.findById(managerId);
            if (!userOptional.isPresent() || userOptional.get().getRoleId() != 3) {
                return new CommonResponse(false, 3, "选择的用户不能成为部门管理员");
            }
            //删除原来的部门管理员
            if (userRepository.updateDepartmentIdAndRoleId(id, 3, savedManagerId) != 1) {       //3:销售
                throw new DepartmentException("修改原部门管理员失败");
            }
            //设置新的部门管理员
            if (userRepository.updateDepartmentIdAndRoleId(id, 2, managerId) != 1) {        //2:部门管理员
                throw new DepartmentException("设置新部门管理员失败");
            }
            //保存部门
            if (departmentRepository.updateDepartment(name, managerId, managerName, id) != 1) {
                throw new DepartmentException("修改部门失败");
            }
            //判断是否修改部门名
            if (!savedName.equals(name)) {      //修改了部门名
                //超级管理员记录行为日志
                commonUtil.addLog(4, "原部门名：" + savedName + "，现部门名：" + name + "，原部门管理员：" + userRepository.findById(savedManagerId).get().getName() + "，现部门管理员：" + managerName, superAdmin.get().getName());
            } else {        //未修改部门名
                //超级管理员记录行为日志
                commonUtil.addLog(4, "原部门管理员：" + userRepository.findById(savedManagerId).get().getName() + "，现部门管理员：" + managerName, superAdmin.get().getName());
            }
        }
        return new CommonResponse();
    }

    /**
     * 查询部门
     * @param page
     * @param size
     * @param department
     * @return
     */
    public CommonResponse search(Integer page, Integer size, Department department) {
        Pageable pageable = new PageRequest(page, size);
        Page<Department> departmentPage = departmentRepository.findAll(new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                //筛选id
                predicateList.add(criteriaBuilder.greaterThan(root.get("id").as(Integer.class), 1));
                Integer id = department.getId();
                if (!commonUtil.isNull(id)) {
                    predicateList.add(criteriaBuilder.equal(root.get("id").as(Integer.class), id));
                }
                //筛选name
                String name = department.getName();
                if (!commonUtil.isNull(name)) {
                    predicateList.add(criteriaBuilder.equal(root.get("name").as(String.class), name));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        }, pageable);
        return new CommonResponse(departmentPage);
    }

    /**
     * 定时任务
     * 部门季度销售量兑现积分
     * @return
     */
    @Scheduled(cron = "0 0 2 * * ?")      //每天2点执行
    @Transactional
    public void salesVolumeToIntegral() {

        //提示信息
        String type = "部门管理员奖励积分";

        //记录开始日志
        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                type,
                "开始",
                new Date()));

        //获取奖励积分数目
        List<Award> awardList = awardRepository.findAll();
        if (awardList == null || awardList.size() != 1) {
            //记录失败日志
            exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                    type,
                    "获取奖励积分数目失败",
                    new Date()));
            throw new DepartmentException("获取奖励积分数目失败");
        }
        Award award = awardList.get(0);
        if (award.getManagerReward() == null || award.getLevelOfReward() == null || award.getSecondaryReward() == null) {
            //记录失败日志
            exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                    type,
                    "获取奖励积分数目失败",
                    new Date()));
            throw new DepartmentException("获取奖励积分数目失败");
        }

        //查询所有部门
        List<Department> departmentList = departmentRepository.findAllByIdGreaterThan(2);       //部门id大于2
        if (departmentList == null || departmentList.size() == 0) {
            //记录失败日志
            exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                    type,
                    "部门不存在",
                    new Date()));
            return;
        }

        //遍历所有部门
        for (Department department : departmentList) {

            //获取信息
            Integer id = department.getId();
            String name = department.getName();
            Integer quarterlySales = department.getQuarterlySales();
            String managerId = department.getManagerId();
            String managerName = department.getManagerName();
            Date createTime = department.getCreateTime();
            Date lastLiquidationTime = department.getLastLiquidationTime();
            Integer flag = department.getFlag();

            //判断是否开始一个季度
            if (createTime.getTime() == lastLiquidationTime.getTime()) {
                continue;       //这个部门还没有下第一个单，还未开始季度，跳过
            }

            String message = "部门id：" + id + "，部门名：" + name + "，部门管理员id：" + managerId + "，部门管理员姓名：" + managerName + "，";
            String message1 = "部门90天内销售量大于等于50";

            //判断上次清算时间和当天相差是否90天
            long days = (new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate()).getTime() -
                    new Date(lastLiquidationTime.getYear(), lastLiquidationTime.getMonth(), lastLiquidationTime.getDate()).getTime()) /
                    (1000 * 3600 * 24);
            if (-days < 90) {        //在季度内
                //判断销售量是否大于等于50并且标志位为0
                if (quarterlySales >= 50 && flag == 0) {
                    //部门管理员在90天内已经卖出大于等于50台车，积分直接到账
                    if (userRepository.addIntegral(quarterlySales * award.getManagerReward(), managerId) != 1) {
                        //记录失败日志
                        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                                type,
                                message + message1 + "[增加积分失败]",
                                new Date()));
                        throw new DepartmentException(message + message1 + "[增加积分失败]");
                    }
                    //记录积分交易
                    if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                            managerId,
                            managerName,
                            id,
                            name,
                            1,
                            quarterlySales * award.getManagerReward(),
                            message1,
                            new Date())) == null) {
                        //记录失败日志
                        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                                type,
                                message + message1 + "[记录积分交易失败]",
                                new Date()));
                        throw new DepartmentException(message + message1 + "[记录积分交易失败]");
                    }
                    //设置标志位为1
                    if (departmentRepository.updateFlag(1, id) != 1) {
                        //记录失败日志
                        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                                type,
                                message + message1 + "[修改标志位为1失败]",
                                new Date()));
                        throw new DepartmentException(message + message1 + "[修改标志位为1失败]");
                    }
                }
            } else {        //到达清算时间
                //判断最后时刻是否满足奖励要求
                if (quarterlySales >= 50 && flag == 0) {
                    //奖励积分到账
                    if (userRepository.addIntegral(quarterlySales * award.getManagerReward(), managerId) != 1) {
                        //记录失败日志
                        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                                type,
                                message + message1 + "[增加积分失败]",
                                new Date()));
                        throw new DepartmentException(message + message1 + "[增加积分失败]");
                    }
                    //记录积分交易
                    if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                            managerId,
                            managerName,
                            id,
                            name,
                            1,
                            quarterlySales * award.getManagerReward(),
                            message1,
                            new Date())) == null) {
                        //记录失败日志
                        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                                type,
                                message + message1 + "[记录积分交易失败]",
                                new Date()));
                        throw new DepartmentException(message + message1 + "[记录积分交易失败]");
                    }
                }
                //清算
                if (departmentRepository.updateQuarterlySalesAndLastLiquidationTimeAndFlag(0, new Date(), 0, id) != 1) {
                    //记录失败日志
                    exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                            type,
                            message + "[清算失败]",
                            new Date()));
                    throw new DepartmentException(message + "[清算失败]");
                }
            }
        }

        //记录结束日志
        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                type,
                "结束",
                new Date()));
    }
}
