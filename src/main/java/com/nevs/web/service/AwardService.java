package com.nevs.web.service;

import com.nevs.web.model.Award;
import com.nevs.web.model.User;
import com.nevs.web.repository.AwardRepository;
import com.nevs.web.repository.UserRepository;
import com.nevs.web.util.CommonResponse;
import com.nevs.web.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author YETA
 * 奖励逻辑处理
 * @date 2018/09/08/19:20
 */
@Service
public class AwardService {

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 修改奖励数目
     * @param userId
     * @param award
     * @return
     */
    public CommonResponse update(String userId, Award award) {

        //判断参数
        if (commonUtil.isNull(award.getId()) || commonUtil.isNull(award.getLevelOfReward()) || commonUtil.isNull(award.getSecondaryReward()) || commonUtil.isNull(award.getManagerReward())) {
            return new CommonResponse(false, 3, "参数不能为空");
        }

        //判断权限
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent() || userOptional.get().getRoleId() != 1) {
            return new CommonResponse(false, 3, "无权修改奖励数目");
        }

        //判断是否存在
        Optional<Award> awardOptional = awardRepository.findById(award.getId());
        if (!awardOptional.isPresent()) {
            return new CommonResponse(false, 3, "id错误");
        }
        Award sAward = awardOptional.get();
        Integer a = sAward.getLevelOfReward();
        Integer b = sAward.getSecondaryReward();
        Integer c= sAward.getManagerReward();

        //修改
        if (awardRepository.save(award) == null) {
            return new CommonResponse(false, 3, "修改奖励数目失败");
        } else {
            String detail = "";
            //记录超级管理员行为日志
            if (award.getLevelOfReward() != a) {
                //记录超级管理员行为日志
                detail += " 一级奖励由：" + a + "修改为：" + award.getLevelOfReward();
            }
            if (award.getSecondaryReward() != b) {
                //记录超级管理员行为日志
                detail += " 二级奖励由：" + b + "修改为：" + award.getSecondaryReward();
            }
            if (award.getManagerReward() != c) {
                //记录超级管理员行为日志
                detail += " 部门管理员奖励由：" + c + "修改为：" + award.getManagerReward();
            }
            commonUtil.addLog(5, detail, userOptional.get().getName());
        }
        return new CommonResponse();
    }

    /**
     * 查询积分奖励数目
     * @param userId
     * @return
     */
    public CommonResponse search(String userId) {
        //判断权限
        if (!commonUtil.verifyAuthority(userId, 1)) {
            return new CommonResponse(false, 3, "无权查询奖励数目");
        }
        return new CommonResponse(awardRepository.findAll());
    }
}
