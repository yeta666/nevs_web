package com.nevs.web.service;

import com.nevs.web.model.ExceptionLog;
import com.nevs.web.repository.ExceptionLogRepository;
import com.nevs.web.util.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author YETA
 * 文件相关操作逻辑实现
 * @date 2018/08/28/21:15
 */
@Service
public class FileService implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
    public static File path;
    public static File upload;
    public static File download;

    /**
     * 在开发测试模式时，得到的地址为：{项目根目录}/target/classes/upload/
     * 在打包成jar正式发布时，得到的地址为：{发布jar包目录}/upload/
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception {
        path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");        //这一句才是精华
        }
        LOG.info("获取项目根目录，{}", path.getAbsolutePath());

        upload = new File(path.getAbsolutePath(), "upload/");
        if (!upload.exists()) {
            LOG.info("获取upload目录失败，创建upload目录，{}", upload.getAbsolutePath());
            upload.mkdirs();
        }
        LOG.info("获取upload目录成功，{}", upload.getAbsolutePath());

        download = new File(path.getAbsolutePath(), "download/");
        if (!download.exists()) {
            LOG.info("获取download目录失败，创建download目录，{}", download.getAbsolutePath());
            download.mkdirs();
        }
        LOG.info("获取download目录成功，{}", download.getAbsolutePath());
    }

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
        File file = new File(upload, dir);
        if (!file.exists()) {
            file.mkdir();
        }
        File file1 = new File(file, System.currentTimeMillis() + "_" + new Random().nextInt() + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
        multipartFile.transferTo(file1);
        //返回图片路径
        return new CommonResponse("/upload/" + dir + file1.getName());
    }

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

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

        if (download.exists()) {
            File[] files = download.listFiles();
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
                    "失败：" + download.getAbsolutePath() + "目录不存在",
                    new Date()));
        }
    }
}
