package com.nevs.web.service;

import com.nevs.web.model.Log;
import com.nevs.web.model.User;
import com.nevs.web.repository.LogRepository;
import com.nevs.web.repository.UserRepository;
import com.nevs.web.util.CommonResponse;
import com.nevs.web.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author YETA
 * 日志相关操作逻辑实现
 * @date 2018/09/03/18:14
 */
@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 查询日志
     * @param page
     * @param size
     * @param userId
     * @param startTime
     * @param endTime
     * @param log
     * @return
     */
    public CommonResponse search(Integer page, Integer size, String userId, Date startTime, Date endTime, Log log) {
        //判断权限
        Optional<User> userOptional  = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return new CommonResponse(false, 3, "无权查询日志");
        }
        Integer roleId = userOptional.get().getRoleId();
        if (roleId != 1 && roleId != 6) {
            return new CommonResponse(false, 3, "无权查询日志");
        }
        //查询
        Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "time"));
        Page<Log> logPage = logRepository.findAll(new Specification<Log>() {
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                //筛选time
                if (!commonUtil.isNull(startTime) && !commonUtil.isNull(endTime)) {
                    predicateList.add(criteriaBuilder.between(root.get("time").as(Date.class), startTime, endTime));
                }
                //筛选action
                Integer action = log.getAction();
                if (!commonUtil.isNull(action)) {
                    predicateList.add(criteriaBuilder.equal(root.get("action").as(Integer.class), action));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        }, pageable);
        return new CommonResponse(logPage);
    }
}
