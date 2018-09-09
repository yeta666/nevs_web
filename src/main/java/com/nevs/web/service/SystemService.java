package com.nevs.web.service;

import com.nevs.web.model.System;
import com.nevs.web.repository.SystemRepository;
import com.nevs.web.util.CommonResponse;
import com.nevs.web.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YETA
 * 系统相关操作逻辑处理
 * @date 2018/08/27/14:27
 */
@Service
public class SystemService {

    @Autowired
    private SystemRepository systemRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 修改系统信息
     * @param system
     * @param userId
     * @return
     */
    public CommonResponse update(System system, String userId) {
        //判断参数
        String id = system.getId();
        String companyProfile = system.getCompanyProfile();
        String homePageImageUrl = system.getHomePageImageUrl();
        String companyAddress = system.getCompanyAddress();
        String contactNumber = system.getContactNumber();
        if(commonUtil.isNull(id) ||
                commonUtil.isNull(companyProfile) ||
                commonUtil.isNull(homePageImageUrl) ||
                commonUtil.isNull(companyAddress) ||
                commonUtil.isNull(contactNumber)){
            return new CommonResponse(false, 3, "系统信息填写不完整");
        }
        //判断权限
        if (!commonUtil.verifyAuthority(userId, 5)) {       //5:系统管理员
            return new CommonResponse(false, 3, "无权修改系统信息");
        }
        //修改
        if (systemRepository.updateSystem(companyProfile, homePageImageUrl, companyAddress, contactNumber, id) != 1) {
            return new CommonResponse(false, 3, "修改系统信息失败");
        }
        return new CommonResponse();
    }

    /**
     * 查询系统信息
     * @return
     */
    public CommonResponse search() {
        return new CommonResponse(systemRepository.findAll());
    }
}
