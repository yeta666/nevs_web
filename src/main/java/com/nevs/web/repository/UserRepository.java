package com.nevs.web.repository;

import com.nevs.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author YETA
 * @date 2018/08/26/14:19
 */
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    User findByName(String name);

    User findByUsername(String username);

    List<User> findAllByDepartmentId(Integer departmentId);

    List<User> findAllByDepartmentIdAndRoleIdNotIn(Integer departmentId, Integer roleId);

    List<User> findAllByInvitationCode(String invitationCode);

    @Modifying
    @Transactional
    @Query("update User u set u.idCardNo = ?1, u.imageUrl1 = ?2, u.imageUrl2 = ?3, u.creditCardNo = ?4, u.bankOfDeposit = ?5, u.phone = ?6, u.roleId = ?7 where u.id = ?8")
    int updateUser(String idCardNo, String imageUrl1, String imageUrl2, String creditCardNo, String bankOfDeposit, String phone, Integer roleId, String id);

    @Modifying
    @Transactional
    @Query("update User u set u.password = ?1 where u.id = ?2")
    int updatePassword(String password, String id);

    @Modifying
    @Transactional
    @Query("update User u set u.departmentId = ?1, u.roleId = ?2 where u.id = ?3")
    int updateDepartmentIdAndRoleId(Integer departmentId, Integer roleId, String id);

    @Modifying
    @Transactional
    @Query("update User u set u.integral = u.integral + ?1 where u.id = ?2")
    int addIntegral(Integer integral, String id);

    @Modifying
    @Transactional
    @Query("update User u set u.integral = u.integral - ?1 where u.id = ?2")
    int reduceIntegral(Integer integer, String id);

    @Modifying
    @Transactional
    @Query("update User u set u.integral = u.integral + ?1, u.totalSales = u.totalSales + ?2 where u.id = ?3")
    int addIntegralAndUpdatTotalSales(Integer integral, Integer quantity, String id);

    @Modifying
    @Transactional
    @Query("update User u set u.integral = u.integral + ?1, u.indirectSales = u.indirectSales + ?2 where u.id = ?3")
    int addIntegralAndUpdatIndirectSales(Integer integral, Integer quantity, String id);
}
