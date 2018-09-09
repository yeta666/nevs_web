package com.nevs.web.util;

import com.nevs.web.model.Log;
import com.nevs.web.model.User;
import com.nevs.web.repository.LogRepository;
import com.nevs.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * @author YETA
 * @date 2018/09/01/14:46
 */
@Component
public class CommonUtil {

    /**
     * 判断是否为空
     * @param data
     * @return
     */
    public boolean isNull(Object data) {
        if (data == null || "".equals(data)) {
            return true;
        }
        return false;
    }

    @Autowired
    private UserRepository userRepository;

    /**
     * 验证权限
     * @param userID
     * @return
     */
    public boolean verifyAuthority(String userID, Integer roleId) {
        Optional<User> userOptional = userRepository.findById(userID);
        if (!userOptional.isPresent() || userOptional.get().getRoleId() != roleId) {
            return false;
        }
        return true;
    }

    @Autowired
    private LogRepository logRepository;

    /**
     * 超级管理员行为记录日志
     * @param action
     * @param detail
     * @param creator
     * @return
     */
    public boolean addLog(int action, String detail, String creator){
        if (logRepository.save(new Log(UUID.randomUUID().toString(),
                action,
                detail,
                new Date(),
                creator)) != null) {
            return true;
        }
        return false;
    }
}
