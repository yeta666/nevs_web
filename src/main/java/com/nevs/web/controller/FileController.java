package com.nevs.web.controller;

import com.nevs.web.service.FileService;
import com.nevs.web.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * @author YETA
 * @date 2018/08/28/16:02
 */
@RestController
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传接口
     * @param file
     * @param type
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/upload")
    public CommonResponse upload(@RequestParam(value = "file") MultipartFile file,
                                 @RequestParam(value = "type") Integer type) throws IOException {
        return fileService.upload(file, type);
    }
}
