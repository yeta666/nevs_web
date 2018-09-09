package com.nevs.web.service;

import com.nevs.web.exception.IntegralException;
import com.nevs.web.model.Department;
import com.nevs.web.model.IntegralTrading;
import com.nevs.web.model.User;
import com.nevs.web.repository.DepartmentRepository;
import com.nevs.web.repository.IntegralTradingRepository;
import com.nevs.web.repository.UserRepository;
import com.nevs.web.util.CommonResponse;
import com.nevs.web.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author YETA
 * 积分相关操作逻辑处理
 * @date 2018/08/28/11:23
 */
@Service
public class IntegralService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IntegralTradingRepository integralTradingRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 提现积分
     * @param userId
     * @param amount
     * @return
     */
    public CommonResponse insert(String userId, Integer amount) {
        //查找用户
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "用户不存在");
        }
        User user = userOptional.get();
        //查找部门
        Integer departmentId = user.getDepartmentId();
        if (commonUtil.isNull(departmentId)) {
            return new CommonResponse(false, 3, "用户未加入部门");
        }
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
        if (!departmentOptional.isPresent()) {
            return new CommonResponse(false, 3, "用户部门不存在");
        }
        //判断权限
        if (user.getRoleId() != 2 && user.getRoleId() != 3) {
            return new CommonResponse(false, 3, "无权提现积分");
        }
        //判断积分
        Integer integral = user.getIntegral();
        if (integral < amount) {
            return new CommonResponse(false, 3, "剩余积分不足");
        }
        if (amount <= 0) {
            return new CommonResponse(false, 3, "提现必须大于0");
        }
        //新增积分交易
        if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                userId,
                user.getName(),
                user.getDepartmentId(),
                departmentOptional.get().getName(),
                2,
                amount,
                new Date(),
                1)) != null) {
            return new CommonResponse();
        }
        return new CommonResponse(false, 3, "提现失败");
    }

    /**
     * 审核提现
     * @param id
     * @return
     */
    public CommonResponse update(String userId, String id) {
        //判断权限
        Optional<User> finance = userRepository.findById(userId);
        if (!finance.isPresent() || finance.get().getRoleId() != 4) {       //4:财务
            return new CommonResponse(false, 3, "无权审核提现");
        }
        //判断积分提现申请是否存在
        Optional<IntegralTrading> integralTradingOptional = integralTradingRepository.findById(id);
        if (!integralTradingOptional.isPresent()) {
            return new CommonResponse(false, 3, "提现申请不存在");
        }
        IntegralTrading integralTrading = integralTradingOptional.get();
        Integer changeWay = integralTrading.getChangeWay();
        Integer changeIntegral = integralTrading.getChangeIntegral();
        //判断是否提现申请
        if (changeWay != 2) {
            return new CommonResponse(false, 3, "非提现申请");
        }
        //判断提现申请状态
        if (integralTrading.getWithdrawStatus() != 1) {
            return new CommonResponse(false, 3, "非待审核提现申请");
        }
        //判断申请提现用户是否存在
        Optional<User> userOptional = userRepository.findById(integralTrading.getUserId());
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "提现用户不存在");
        }
        User user = userOptional.get();
        //判断申请提现用户角色
        if (user.getRoleId() != 2 && user.getRoleId() != 3) {
            return new CommonResponse(false, 3, "提现失败");
        }
        //判断积分
        if (user.getIntegral() < changeIntegral) {
            return new CommonResponse(false, 3, "剩余积分不足");
        }
        //减少积分
        if (userRepository.reduceIntegral(changeIntegral, user.getId()) != 1) {
            return new CommonResponse(false, 3, "减少积分失败");
        }
        //修改提现交易
        integralTrading.setWithdrawStatus(2);
        integralTrading.setAuditor(finance.get().getName());
        integralTrading.setAuditPassTime(new Date());
        if (integralTradingRepository.save(integralTrading) != null) {
            return new CommonResponse();
        }
        throw new IntegralException("审核提现失败");
    }

    /**
     * 查询积分
     * @param userId
     * @return
     */
    public CommonResponse search(String userId) {
        //查询用户
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "用户不存在");
        }
        User user = userOptional.get();
        //判断权限
        if (user.getRoleId() != 2 && user.getRoleId() != 3) {
            return new CommonResponse(false, 3, "无权限查询积分");
        }
        return new CommonResponse(user.getIntegral());
    }

    /**
     * 查询积分交易
     * @param page
     * @param size
     * @param integralTrading
     * @return
     */
    public CommonResponse searchTrading(Integer page, Integer size, IntegralTrading integralTrading) {
        Pageable pageable = new PageRequest(page, size);
        Page<IntegralTrading> integralTradingPage = integralTradingRepository.findAll(new Specification<IntegralTrading>() {
            @Override
            public Predicate toPredicate(Root<IntegralTrading> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                //筛选userId
                String userId = integralTrading.getUserId();
                if (!commonUtil.isNull(userId)) {
                    predicateList.add(criteriaBuilder.equal(root.get("userId").as(String.class), userId));
                }
                //筛选userName
                String userName = integralTrading.getUserName();
                if (!commonUtil.isNull(userName)) {
                    predicateList.add(criteriaBuilder.equal(root.get("userName").as(String.class), userName));
                }
                //筛选changeWay
                Integer changeWay = integralTrading.getChangeWay();
                if (!commonUtil.isNull(changeWay)) {
                    predicateList.add(criteriaBuilder.equal(root.get("changeWay").as(Integer.class), changeWay));
                }
                //筛选orderNo
                String orderNo = integralTrading.getOrderNo();
                if (!commonUtil.isNull(orderNo)) {
                    predicateList.add(criteriaBuilder.equal(root.get("orderNo").as(String.class), orderNo));
                }
                //筛选withdrawStatus
                Integer withdrawStatus = integralTrading.getWithdrawStatus();
                if (!commonUtil.isNull(withdrawStatus)) {
                    predicateList.add(criteriaBuilder.equal(root.get("withdrawStatus").as(Integer.class), withdrawStatus));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        }, pageable);
        return new CommonResponse(integralTradingPage);
    }
}
