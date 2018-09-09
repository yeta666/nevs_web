package com.nevs.web.service;

import com.nevs.web.model.ExceptionLog;
import com.nevs.web.repository.ExceptionLogRepository;
import com.nevs.web.util.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author YETA
 * 文件相关操作逻辑实现
 * @date 2018/08/28/21:15
 */
@Service
public class FileService {

    @Value("${nevs.upload}")
    private String upload;

    @Value("${nevs.download}")
    private String download;

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    /**
     * 文件上传
     * @param multipartFile
     * @param type
     * @return
     * @throws IOException
     */
    public CommonResponse upload(MultipartFile multipartFile, Integer type) throws IOException {
        String dir;
        //判断文件类型
        if (type == 1) {        //用户
            dir = "user/";
        } else if (type == 2) {     //商品
            dir = "vehicle/";
        } else if (type == 3) {     //订单
            dir = "order/";
        } else if (type == 4) {     //系统
            dir = "system/";
        } else {
            return new CommonResponse(false, 3, "上传文件失败");
        }
        //保存文件
        File file = new File(upload + dir +
                System.currentTimeMillis() + "_" + new Random().nextInt() + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
        multipartFile.transferTo(file);
        //返回图片路径
        return new CommonResponse("/upload/" + dir + file.getName());
    }

    /**
     * 定时任务
     * 清空download文件夹
     */
    @Scheduled(cron = "0 0 3 * * ?")      //每天3点执行
    public void clean() {
        //记录开始日志
        exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                "清空download文件夹",
                "开始",
                new Date()));

        File dir = new File(download);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }

            //记录结束日志
            exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                    "清空download文件夹",
                    "结束",
                    new Date()));
        } else {
            //记录失败日志
            exceptionLogRepository.save(new ExceptionLog(UUID.randomUUID().toString(),
                    "清空download文件夹",
                    "失败：" + download + "目录不存在",
                    new Date()));
        }
    }
}
