package com.nevs.web.repository;

import com.nevs.web.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author YETA
 * @date 2018/08/27/14:27
 */
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    List<Order> findAllByOrderExpireBetween(Integer down, Integer up);

    @Modifying
    @Transactional
    @Query("update Order o set o.orderStatus = ?1, o.reasonOfCanNotPass = ?2 where o.orderNo = ?3")
    int updateOrderStatusAndReasonOfCanNotPass(Integer orderStatus, String reasonOfCanNotPass, String orderNo);

    @Transactional
    @Modifying
    @Query("update Order o set o.orderExpire = ?1 where o.orderNo = ?2")
    int updateOrderExpire(Integer orderExpire, String orderNo);
}
