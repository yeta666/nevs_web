package com.nevs.web.repository;

import com.nevs.web.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author YETA
 * @date 2018/09/02/14:40
 */
public interface LogRepository extends JpaRepository<Log, String>, JpaSpecificationExecutor<Log> {
}
