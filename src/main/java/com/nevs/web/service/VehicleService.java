package com.nevs.web.service;

import com.nevs.web.model.Vehicle;
import com.nevs.web.repository.VehicleRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author YETA
 * 车辆相关操作逻辑处理
 * @date 2018/08/26/17:00
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 新增商品
     * @param userId
     * @param vehicle
     * @return
     */
    public CommonResponse insert(String userId, Vehicle vehicle) {
        //判断参数
        if (commonUtil.isNull(vehicle.getName()) || commonUtil.isNull(vehicle.getPrice()) || commonUtil.isNull(vehicle.getSubscription()) || commonUtil.isNull(vehicle.getStore())) {
            return new CommonResponse(false, 3, "商品名称、价格、订金、是否有库存不能为空");
        }
        //判断当前用户是否有是系统管理员
        if (!commonUtil.verifyAuthority(userId, 5)) {       //5:系统管理员
            return new CommonResponse(false, 3, "无权新增商品");
        }
        //补全信息
        vehicle.setId(UUID.randomUUID().toString());
        vehicle.setQuantityOfSale(0);
        //保存
        if (vehicleRepository.save(vehicle) != null) {
            return new CommonResponse();
        }
        return new CommonResponse(false, 3, "新增商品失败");
    }

    /**
     * 修改商品
     * @param userId
     * @param vehicle
     * @return
     */
    public CommonResponse update(String userId, Vehicle vehicle) {
        //判断参数
        if (commonUtil.isNull(vehicle.getName()) || commonUtil.isNull(vehicle.getPrice()) || commonUtil.isNull(vehicle.getSubscription()) || commonUtil.isNull(vehicle.getStore())) {
            return new CommonResponse(false, 3, "商品名称、价格、订金、是否有库存不能为空");
        }
        //判断商品是否存在
        String id = vehicle.getId();
        if (commonUtil.isNull(id) || !vehicleRepository.findById(id).isPresent()) {
            return new CommonResponse(false, 3, "商品不存在");
        }
        //判断当前用户是否有是系统管理员
        if (!commonUtil.verifyAuthority(userId, 5)) {
            return new CommonResponse(false, 3, "无权修改商品");
        }
        //修改
        if (vehicleRepository.updateVehicle(vehicle.getName(),
                vehicle.getDescription(),
                vehicle.getPrice(),
                vehicle.getSubscription(),
                vehicle.getImageUrl(),
                vehicle.getStore(),
                vehicle.getId()) == 1) {
            return new CommonResponse();
        }
        return new CommonResponse(false, 3, "修改商品失败");
    }

    /**
     * 查询商品
     * @param page
     * @param size
     * @param vehicle
     * @return
     */
    public CommonResponse search(Integer page, Integer size, Vehicle vehicle) {
        Pageable pageable = new PageRequest(page, size);
        Page<Vehicle> vehiclePage = vehicleRepository.findAll(new Specification<Vehicle>() {
            @Override
            public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                //筛选id
                String id = vehicle.getId();
                if (!commonUtil.isNull(id)) {
                    predicateList.add(criteriaBuilder.equal(root.get("id").as(String.class), id));
                }
                //筛选name
                String name = vehicle.getName();
                if (!commonUtil.isNull(name)) {
                    predicateList.add(criteriaBuilder.like(root.get("name").as(String.class), "%"+name+"%"));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        }, pageable);
        return new CommonResponse(vehiclePage);
    }
}
