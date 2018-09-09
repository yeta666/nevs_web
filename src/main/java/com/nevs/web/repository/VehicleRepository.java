package com.nevs.web.repository;

import com.nevs.web.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YETA
 * @date 2018/08/26/16:59
 */
public interface VehicleRepository extends JpaRepository<Vehicle, String>, JpaSpecificationExecutor<Vehicle> {

    @Modifying
    @Transactional
    @Query("update Vehicle v set v.quantityOfSale = v.quantityOfSale + ?1 where v.id = ?2")
    int updateQuantityOfSale(Integer quantity, String id);

    @Modifying
    @Transactional
    @Query("update Vehicle v set v.name = ?1, v.description = ?2, v.price = ?3, v.subscription = ?4, v.imageUrl = ?5, v.store = ?6 where v.id = ?7")
    int updateVehicle(String name, String description, Double price, Double subscription, String imageUrl, Integer store, String id);
}
