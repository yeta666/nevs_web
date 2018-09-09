package com.nevs.web.exception;

import com.nevs.web.model.ExceptionLog;
import com.nevs.web.repository.ExceptionLogRepository;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.UUID;

/**
 * @author YETA
 * 统一异常处理
 * @date 2018/08/26/15:22
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    /**
     * 默认异常处理
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResponse defaultExceptionHandler(Exception e) throws Exception {
        //如果不是自定义异常，就记录在数据库中
        boolean a = e instanceof DepartmentException;
        boolean b = e instanceof OrderException;
        boolean c = e instanceof IntegralException;
        boolean d = e instanceof UserException;
        if (!a && !b && !c && !d) {
            exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                    e.toString().split(":")[0],
                    e.getMessage(),
                    new Date()));
        }
        e.printStackTrace();
        return new CommonResponse(false, 4, e.getMessage());
    }
}
