package com.nevs.web.service;

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
 * 订单相关操作逻辑处理
 * @date 2018/08/27/14:27
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IntegralTradingRepository integralTradingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 新增订单
     * @param order
     * @return
     */
    @Transactional
    public CommonResponse insert(Order order) {
        //判断参数
        String orderNo = order.getOrderNo();
        String salesId = order.getSalesId();
        String vehicleId = order.getVehicleId();
        if (commonUtil.isNull(orderNo) ||
                commonUtil.isNull(salesId) ||
                commonUtil.isNull(vehicleId) ||
                commonUtil.isNull(order.getOrderImageUrl()) ||
                commonUtil.isNull(order.getPrice()) ||
                commonUtil.isNull(order.getQuantity())) {
            return new CommonResponse(false, 3, "订单信息填写不完整");
        }
        //判断订单号是否已存在
        if (orderRepository.findById(orderNo).isPresent()) {
            return new CommonResponse(false, 3, "订单号已存在");
        }
        //判断下单人id是否存在
        Optional<User> userOptional = userRepository.findById(salesId);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "下单用户不存在");
        }
        //判断下单人部门是否存在
        Integer departmentId = userOptional.get().getDepartmentId();
        if (commonUtil.isNull(departmentId) || departmentId < 2 || !departmentRepository.findById(departmentId).isPresent()) {
            return new CommonResponse(false, 3, "下单人部门不存在");
        }
        //判断下单商品是否存在
        if (!vehicleRepository.findById(vehicleId).isPresent()) {
            return new CommonResponse(false, 3, "下单商品不存在");
        }
        //设置订单状态为待审核
        order.setOrderStatus(0);
        //设置部门id
        order.setDepartmentId(departmentId);
        //设置订单过期状态
        order.setOrderExpire(getOrderExpire(order));
        //保存
        if (orderRepository.save(order) != null) {
            return new CommonResponse();
        }
        return new CommonResponse(false, 3, "新增订单失败");
    }

    /**
     * 修改订单
     * @param userId
     * @param pass
     * @param order
     * @return
     */
    @Transactional
    public CommonResponse update(String userId, Integer pass, Order order) {
        //判断参数
        String orderNo = order.getOrderNo();
        String salesId = order.getSalesId();
        String vehicleId = order.getVehicleId();
        Integer orderStatus = order.getOrderStatus();
        Integer orderExpire = order.getOrderExpire();
        if (commonUtil.isNull(orderNo) || commonUtil.isNull(salesId) || commonUtil.isNull(vehicleId) || commonUtil.isNull(orderStatus) || commonUtil.isNull(orderExpire)) {
            return new CommonResponse(false, 3, "订单号、销售id、商品id、订单状态、订单过期状态不能为空");
        }
        //判断订单是否存在
        Optional<Order> orderOptional = orderRepository.findById(orderNo);
        if (!orderOptional.isPresent()) {
            return new CommonResponse(false, 3, "订单不存在");
        }
        Order sOrder = orderOptional.get();
        //判断不可修改信息是否正确
        if (!salesId.equals(sOrder.getSalesId()) ||
                !vehicleId.equals(sOrder.getVehicleId()) ||
                orderStatus != sOrder.getOrderStatus() ||
                orderStatus == 1 ||     //如果订单已经审核通过
                orderExpire != sOrder.getOrderExpire()) {
            return new CommonResponse(false, 3, "提交订单信息与原订单信息不符");
        }
        //判断执行哪种修改
        if (!commonUtil.isNull(userId) && !commonUtil.isNull(pass)) {     //审核订单
            return auditOrder(userId, pass, order.getReasonOfCanNotPass(), sOrder);
        } else if (commonUtil.isNull(userId) && commonUtil.isNull(pass)){        //修改订单信息
            //设置订单状态待审核
            order.setOrderStatus(0);
            order.setReasonOfCanNotPass(null);
            //保存
            if (orderRepository.save(order) != null) {
                return new CommonResponse();
            }
        }
        return new CommonResponse(false, 3, "修改订单信息失败");
    }

    /**
     * 审核订单
     * @param userId
     * @param pass
     * @param reasonOfCanNotPass
     * @param sOrder
     * @return
     */
    public CommonResponse auditOrder(String userId, Integer pass, String reasonOfCanNotPass, Order sOrder) {

        //判断权限
        Optional<User> finance = userRepository.findById(userId);
        if (!finance.isPresent() || finance.get().getRoleId() != 4) {       //4:财务
            return new CommonResponse(false, 3, "您没有权限审核订单");
        }

        //订单号
        String orderNo = sOrder.getOrderNo();

        //审核订单过程
        if (pass == 1) {      //审核通过

            //修改订单状态为审核通过
            if (orderRepository.updateOrderStatusAndReasonOfCanNotPass(1, reasonOfCanNotPass, orderNo) != 1) {
                throw new OrderException("修改订单状态为审核通过失败");
            }

            //增加商品销售量
            Integer quantity = sOrder.getQuantity();
            if (commonUtil.isNull(quantity) || vehicleRepository.updateQuantityOfSale(quantity, sOrder.getVehicleId()) != 1) {
                throw new OrderException("增加商品销售量失败");
            }

            //获取该订单的销售
            Optional<User> userOptional1 = userRepository.findById(sOrder.getSalesId());
            if (!userOptional1.isPresent()) {
                throw new OrderException("该订单的销售不存在");
            }
            User user1 = userOptional1.get();

            //获取该订单的销售的部门
            Integer departmentId = user1.getDepartmentId();
            if (commonUtil.isNull(departmentId) || departmentId < 2) {
                throw new OrderException("该订单的销售的部门不正确");
            }
            Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
            if (!departmentOptional.isPresent()) {
                throw new OrderException("该订单的销售的部门不存在");
            }
            Department department = departmentOptional.get();
            String departmentName = department.getName();

            //股东购车
            if (user1.getRoleId() == 8) {
                Integer carIntegral = user1.getCarIntegral();
                Integer deductionPrice = sOrder.getDeductionPrice();
                //判断购车积分
                if (carIntegral < deductionPrice) {
                    throw new OrderException("股东购车可用抵扣积分不足");
                } else {
                    //减少购车积分
                    user1.setCarIntegral(carIntegral - deductionPrice);
                    if (userRepository.save(user1) != null) {
                        //记录积分交易
                        if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                                user1.getId(),
                                user1.getName(),
                                departmentId,
                                departmentName,
                                2,
                                deductionPrice,
                                "股东购车，抵扣购车积分",
                                new Date())) != null) {
                            return new CommonResponse();
                        } else {
                            throw new OrderException("记录积分交易失败");
                        }
                    } else {
                        throw new OrderException("减少股东购车可用抵扣积分失败");
                    }
                }
            }

            //判断该订单是否该部门的第一单
            if (department.getTotalSales() == 0 && department.getQuarterlySales() == 0 && department.getCreateTime().getTime() == department.getLastLiquidationTime().getTime() && department.getFlag() == 0) {
                //开始按90天一个季度计算
                if (departmentRepository.updateLastLiquidationTime(new Date(), departmentId) != 1) {
                    throw new OrderException("该订单是该销售的部门的第一单，设置清算时间失败");
                }
            }

            //增加该部门总销售量和季度销售量
            if (departmentRepository.updateSales(quantity, departmentId) != 1) {
                throw new OrderException("增加部门总销售量和季度销售量失败");
            }

            //获取奖励积分数目
            List<Award> awardList = awardRepository.findAll();
            if (awardList == null || awardList.size() != 1) {
                throw new OrderException("获取奖励积分数目失败");
            }
            Award award = awardList.get(0);
            if (award.getManagerReward() == null || award.getLevelOfReward() == null || award.getSecondaryReward() == null) {
                throw new OrderException("获取奖励积分数目失败");
            }

            //判断该部门季度销售量是否大于等于50，并且标志位为1
            if (department.getQuarterlySales() >= 50 && department.getFlag() == 1) {
                //部门管理员奖励积分
                if (userRepository.addIntegral(quantity * award.getManagerReward(), department.getManagerId()) != 1) {
                    throw new OrderException("部门管理员奖励积分失败");
                }
                //记录积分交易
                if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                        department.getManagerId(),
                        department.getManagerName(),
                        departmentId,
                        departmentName,
                        1,
                        quantity * award.getManagerReward(),
                        "部门90天内销售量大于等于50，超出部分直接奖励",
                        new Date())) == null) {
                    throw new OrderException("记录部门管理员奖励积分交易失败");
                }
            }

            //判断该订单的销售是否是部门管理员
            if (!sOrder.getSalesId().equals(department.getManagerId())) {

                String integralTradingId = null;

                //获取该订单的销售的邀请人
                if (user1.getInvitationCode() == null) {
                    throw new OrderException("该销售的邀请码为空");
                }
                Optional<User> userOptional2 = userRepository.findById(user1.getInvitationCode());
                if (!userOptional2.isPresent()) {
                    throw new OrderException("获取该订单的销售的邀请人失败");
                }
                User user2 = userOptional2.get();

                //判断该订单的销售的邀请人是否有资格获得积分
                if (user2.getRoleId() == 2 || user2.getRoleId() == 3) {     //2:部门管理员，3:销售
                    //判断该订单的销售的邀请人是否和该销售是同一个部门
                    if (user2.getDepartmentId() != departmentId) {
                        throw new OrderException("该订单的销售与该订单的销售的邀请人不是同一个部门");
                    }
                    //第一级，增加 3000 * 车辆数量 积分，并且修改该销售的邀请人的销售量
                    if (userRepository.addIntegralAndUpdatTotalSales(award.getLevelOfReward() * quantity, quantity, user2.getId()) != 1) {
                        throw new OrderException("增加该销售的邀请人的销售量失败");
                    }
                    //记录积分交易细节
                    IntegralTrading integralTrading1 = integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                            user2.getId(),
                            user2.getName(),
                            departmentId,
                            departmentName,
                            1,
                            award.getLevelOfReward() * quantity,
                            finance.get().getName(),
                            new Date(),
                            orderNo));
                    integralTradingId = integralTrading1.getId();
                    if (integralTrading1 == null) {
                        throw new OrderException("记录积分交易细节失败1");
                    }
                }

                //获取该订单的销售的邀请人的邀请人
                if (user2.getInvitationCode() == null) {
                    throw new OrderException("该销售的邀请人的邀请码为空");
                }
                Optional<User> userOptional3 = userRepository.findById(user2.getInvitationCode());
                if (!userOptional3.isPresent()) {
                    throw new OrderException("获取该订单的销售的邀请人的邀请人失败");
                }
                User user3 = userOptional3.get();

                //判断该订单的销售的邀请人的邀请人是否有资格获取积分
                if (user3.getRoleId() == 2 || user3.getRoleId() == 3) {
                    //判断该订单的销售的邀请人的邀请人是否和该销售是同一个部门
                    if (user3.getDepartmentId() != departmentId) {
                        throw new OrderException("该订单的销售与该订单的销售的邀请人的邀请人不是同一个部门");
                    }
                    //第二级，增加 1000 * 车辆数量 积分，并且并且修改该销售的邀请人的邀请人的间接销售量
                    if (userRepository.addIntegralAndUpdatIndirectSales(award.getSecondaryReward() * quantity, quantity, user3.getId()) != 1) {
                        throw new OrderException("增加该订单的销售人的邀请人的邀请人的积分失败");
                    }
                    //记录积分交易细节
                    if (integralTradingRepository.save(new IntegralTrading(UUID.randomUUID().toString(),
                            user3.getId(),
                            user3.getName(),
                            departmentId,
                            departmentName,
                            1,
                            award.getSecondaryReward() * quantity,
                            finance.get().getName(),
                            new Date(),
                            orderNo,
                            integralTradingId)) == null) {
                        throw new OrderException("记录积分交易细节失败2");
                    }
                }
            }
            return new CommonResponse();
        } else if (pass == 2) {       //审核未通过
            //修改订单状态审核未通过
            if (orderRepository.updateOrderStatusAndReasonOfCanNotPass(2, reasonOfCanNotPass, orderNo) == 1) {
                return new CommonResponse();
            } else {
                return new CommonResponse(false, 3, "审核不通过失败");
            }
        } else {
            return new CommonResponse(false, 3, "pass错误");
        }
    }

    /**
     * 查询订单
     * @param page
     * @param size
     * @param order
     * @param relation
     * @return
     */
    public CommonResponse search(Integer page, Integer size, Order order, Integer relation) {
        //判断参数
        String salesId = order.getSalesId();
        if (!commonUtil.isNull(salesId)) {
            Optional<User> userOptional = userRepository.findById(salesId);
            if (userOptional.isPresent() && userOptional.get().getRoleId() != 3 && userOptional.get().getRoleId() != 2 && userOptional.get().getRoleId() != 8) {
                return new CommonResponse(false, 3, "不能查询管理用户");
            }
        }
        Integer departmentId = order.getDepartmentId();
        if (!commonUtil.isNull(departmentId) && departmentId < 2) {
            return new CommonResponse(false, 3, "不能查询管理部门");
        }
        //判断是哪种查询
        if (commonUtil.isNull(relation)) {
            //查询
            Pageable pageable = new PageRequest(page, size);
            Page<Order> orderPage = orderRepository.findAll(new Specification<Order>() {
                @Override
                public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<Predicate>();
                    //过滤salesId
                    if (!commonUtil.isNull(salesId)) {
                        predicateList.add(criteriaBuilder.equal(root.get("salesId").as(String.class), salesId));
                    }
                    //筛选departmentId
                    if (!commonUtil.isNull(departmentId)) {
                        predicateList.add(criteriaBuilder.equal(root.get("departmentId").as(Integer.class), departmentId));
                    }
                    //过滤orderNo
                    String orderNo = order.getOrderNo();
                    if (!commonUtil.isNull(orderNo)) {
                        predicateList.add(criteriaBuilder.equal(root.get("orderNo").as(String.class), orderNo));
                    }
                    //过滤orderExpire
                    Integer orderExpire = order.getOrderExpire();
                    if (!commonUtil.isNull(orderExpire)) {
                        predicateList.add(criteriaBuilder.equal(root.get("orderExpire").as(Integer.class), orderExpire));
                    }
                    //过滤orderStatus
                    Integer orderStatus = order.getOrderStatus();
                    if (!commonUtil.isNull(orderStatus)) {
                        predicateList.add(criteriaBuilder.equal(root.get("orderStatus").as(Integer.class), orderStatus));
                    }
                    Predicate[] predicates = new Predicate[predicateList.size()];
                    return criteriaBuilder.and(predicateList.toArray(predicates));
                }
            }, pageable);
            return new CommonResponse(orderPage);
        } else if (relation == 1) {
            Pageable pageable = new PageRequest(page, size);
            //查询所有有关联的用户id
            List<String> ids = new ArrayList<>();
            List<User> userList = userRepository.findAllByInvitationCode(salesId);
            for (User user : userList) {
                ids.add(user.getId());
                List<User> userList1 = userRepository.findAllByInvitationCode(user.getId());
                for (User user1 : userList1) {
                    ids.add(user1.getId());
                }
            }
            //查询
            Page<Order> orderPage = orderRepository.findAll(new Specification<Order>() {
                @Override
                public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    //or条件
                    List<Predicate> predicateList1 = new ArrayList<Predicate>();
                    //筛选salesId
                    for (String id : ids) {
                        predicateList1.add(criteriaBuilder.equal(root.get("salesId").as(String.class), id));
                    }
                    Predicate[] predicates1 = new Predicate[predicateList1.size()];

                    //and条件
                    List<Predicate> predicateList2 = new ArrayList<Predicate>();
                    //过滤orderNo
                    String orderNo = order.getOrderNo();
                    if (!commonUtil.isNull(orderNo)) {
                        predicateList2.add(criteriaBuilder.equal(root.get("orderNo").as(String.class), orderNo));
                    }
                    //过滤orderExpire
                    Integer orderExpire = order.getOrderExpire();
                    if (!commonUtil.isNull(orderExpire)) {
                        predicateList2.add(criteriaBuilder.equal(root.get("orderExpire").as(Integer.class), orderExpire));
                    }
                    //过滤orderStatus
                    Integer orderStatus = order.getOrderStatus();
                    if (!commonUtil.isNull(orderStatus)) {
                        predicateList2.add(criteriaBuilder.equal(root.get("orderStatus").as(Integer.class), orderStatus));
                    }
                    Predicate[] predicates2 = new Predicate[predicateList2.size()];

                    //组合条件
                    Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(predicates1));
                    Predicate predicate2 = criteriaBuilder.and(predicateList2.toArray(predicates2));
                    return criteriaBuilder.and(predicate1, predicate2);
                }
            }, pageable);
            return new CommonResponse(orderPage);
        } else {
            return new CommonResponse(false, 3, "查询订单错误");
        }
    }

    /**
     * 获取订单过期状态
     * @param order
     * @return
     */
    public int getOrderExpire(Order order) {
        double a = order.getGiveVehicleDate().getTime();
        Date today = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate());
        double b = today.getTime();
        double c = order.getOrderTime().getTime();
        //如果交车时间小于当天时间，订单已过期
        if (a - b < 0) {
            return 2;
        } else {
            //如果交车时间等于订单时间，订单即将过期
            if (a - c == 0) {
                return 1;
            } else {
                if ((a - b) / (a - c) <= 0.2) {
                    return 1;
                }
            }

        }
        return 0;
    }

    /**
     * 定时任务
     * 修改订单是否过期
     */
    //@Scheduled(fixedRate = 1000 * 60)        //每60秒执行一次
    @Scheduled(cron = "0 0 4 * * ?")      //每天4点执行
    public void updateOrderDue() {
        //记录开始日志
        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                "修改订单是否过期",
                "开始",
                new Date()));

        //获取所有未过期订单
        List<Order> orderList = orderRepository.findAllByOrderExpireBetween(0, 1);
        if (orderList == null || orderList.size() == 0) {
            //记录失败日志
            exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                    "修改订单是否过期",
                    "失败：不存在状态为未过期的订单",
                    new Date()));
            return;
        }

        //遍历所有未过期订单
        for (Order order : orderList) {
            if (getOrderExpire(order) != order.getOrderExpire()) {
                Integer orderExpire = getOrderExpire(order);
                if (orderRepository.updateOrderExpire(orderExpire, order.getOrderNo()) != 1) {
                    //记录失败日志
                    exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                            "修改订单是否过期",
                            "失败：订单号为：" + order.getOrderNo() + "的订单过期状态修改失败",
                            new Date()));
                } else {
                    //记录修改日志
                    exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                            "修改订单是否过期",
                            "修改：订单号为：" + order.getOrderNo() + "的订单过期状态由：" + order.getOrderExpire() + "修改为：" + orderExpire,
                            new Date()));
                }
            }
        }

        //记录结束日志
        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                "修改订单是否过期",
                "结束",
                new Date()));
    }
}
