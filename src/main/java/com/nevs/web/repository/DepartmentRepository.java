package com.nevs.web.repository;

import com.nevs.web.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author YETA
 * @date 2018/08/26/16:02
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {

    Department findByName(String name);

    List<Department> findAllByIdGreaterThan(Integer id);

    @Modifying
    @Transactional
    @Query("delete from Department d where d.id = ?1")
    int deleteDepartment(Integer id);

    @Modifying
    @Transactional
    @Query("update Department d set d.name = ?1, d.managerId = ?2, d.managerName = ?3 where d.id = ?4")
    int updateDepartment(String name, String managerId, String managerName, Integer id);

    @Modifying
    @Transactional
    @Query("update Department d set d.lastLiquidationTime = ?1 where d.id = ?2")
    int updateLastLiquidationTime(Date lastLiquidationTime, Integer id);

    @Modifying
    @Transactional
    @Query("update Department d set d.totalSales = d.totalSales + ?1, d.quarterlySales = d.quarterlySales + ?1 where d.id = ?2")
    int updateSales(Integer amount, Integer id);

    @Modifying
    @Transactional
    @Query("update Department d set d.flag = ?1 where d.id = ?2")
    int updateFlag(Integer flag, Integer id);

    @Modifying
    @Transactional
    @Query("update Department d set d.quarterlySales = ?1, d.lastLiquidationTime = ?2, d.flag = ?3 where d.id = ?4")
    int updateQuarterlySalesAndLastLiquidationTimeAndFlag(Integer quarterlySales, Date lastLiquidationTime, Integer flag, Integer id);
}
