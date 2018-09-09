package com.nevs.web.repository;

import com.nevs.web.model.IntegralTrading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author YETA
 * @date 2018/08/27/16:27
 */
public interface IntegralTradingRepository extends JpaRepository<IntegralTrading, String>, JpaSpecificationExecutor<IntegralTrading> {
}
