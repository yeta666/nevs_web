package com.nevs.web.service;

import com.nevs.web.model.Notice;
import com.nevs.web.repository.NoticeRepository;
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
import java.util.UUID;

/**
 * @author YETA
 * 公告相关操作逻辑处理
 * @date 2018/08/27/14:27
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 新增公告
     * @param notice
     * @param userId
     * @return
     */
    public CommonResponse insert(Notice notice, String userId) {
        //判断权限
        if (!commonUtil.verifyAuthority(userId, 5)) {       //5:系统管理员
            return new CommonResponse(false, 3, "无权新增公告");
        }
        //判断参数
        String title = notice.getTitle();
        String content = notice.getContent();
        if (commonUtil.isNull(title) || commonUtil.isNull(content)) {
            return new CommonResponse(false, 3, "标题和内容不能为空");
        }
        //信息补全
        notice.setId(UUID.randomUUID().toString());
        notice.setAnnounceTime(new Date());
        //保存
        if (noticeRepository.save(notice) == null) {
            return new CommonResponse(false, 3, "新增公告失败");
        }
        return new CommonResponse();
    }

    /**
     * 修改公告
     * @param notice
     * @param userId
     * @return
     */
    public CommonResponse update(Notice notice, String userId) {
        //判断权限
        if (!commonUtil.verifyAuthority(userId, 5)) {       //5:系统管理员
            return new CommonResponse(false, 3, "无权修改公告");
        }
        //判断参数
        String id = notice.getId();
        String title = notice.getTitle();
        String content = notice.getContent();
        if (commonUtil.isNull(id) || commonUtil.isNull(title) || commonUtil.isNull(content)) {
            return new CommonResponse(false, 3, "标题和内容不能为空");
        }
        //修改
        if (noticeRepository.updateNotice(title, content, id) !=  1) {
            return new CommonResponse(false, 3, "修改公告失败");
        }
        return new CommonResponse();
    }

    /**
     * 搜索公告
     * @param page
     * @param size
     * @param notice
     * @return
     */
    public CommonResponse search(Integer page, Integer size, Notice notice, Date startTime, Date endTime) {
        Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "announceTime"));
        Page<Notice> noticePage = noticeRepository.findAll(new Specification<Notice>() {
            @Override
            public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                //筛选id
                String id = notice.getId();
                if (!commonUtil.isNull(id)) {
                    predicateList.add(criteriaBuilder.equal(root.get("id").as(String.class), id));
                }
                //筛选title
                String title = notice.getTitle();
                if (!commonUtil.isNull(title)) {
                    predicateList.add(criteriaBuilder.like(root.get("title").as(String.class), "%"+title+"%"));
                }
                //筛选content
                String content = notice.getContent();
                if (!commonUtil.isNull(content)) {
                    predicateList.add(criteriaBuilder.like(root.get("content").as(String.class), "%"+content+"%"));
                }
                //筛选announceTime
                if (!commonUtil.isNull(startTime) && !commonUtil.isNull(endTime)) {
                    predicateList.add(criteriaBuilder.between(root.get("announceTime").as(Date.class), startTime, endTime));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        }, pageable);
        return new CommonResponse(noticePage);
    }
}
