package com.nevs.web.repository;

import com.nevs.web.model.System;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YETA
 * @date 2018/08/26/14:06
 */
public interface SystemRepository extends JpaRepository<System, Integer>{

    @Modifying
    @Transactional
    @Query("update System s set s.companyProfile = ?1, s.homePageImageUrl = ?2, s.companyAddress = ?3, s.contactNumber = ?4 where s.id = ?5")
    int updateSystem(String companyProfile, String homePageImageUrl, String companyAddress, String contactNumber, String id);

}
