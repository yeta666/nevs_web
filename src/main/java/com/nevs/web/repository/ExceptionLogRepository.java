package com.nevs.web.repository;

import com.nevs.web.model.ExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author YETA
 * @date 2018/09/05/14:13
 */
public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, String> {
}
