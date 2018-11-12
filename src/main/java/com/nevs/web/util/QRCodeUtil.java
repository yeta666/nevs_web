package com.nevs.web.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nevs.web.service.FileService;
import org.springframework.stereotype.Component;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;

@Component
public class QRCodeUtil {

    public String getQRCode(String content, int width, int height){
        String format = "png";

        //定义二维码的参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN,2);

        String name = "";
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
            Path file = new File(FileService.download, System.currentTimeMillis() + "_" + new Random().nextInt() + ".png").toPath();
            name = file.getFileName().toString();
            MatrixToImageWriter.writeToPath(bitMatrix,format,file);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "/download/" + name;
    }
}
