package com.nevs.web.repository;

import com.nevs.web.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YETA
 * @date 2018/08/26/14:06
 */
public interface NoticeRepository  extends JpaRepository<Notice, String> , JpaSpecificationExecutor<Notice> {

    @Modifying
    @Transactional
    @Query("update Notice n set n.title = ?1, n.content = ?2 where n.id = ?3")
    int updateNotice(String title, String content, String id);
}
