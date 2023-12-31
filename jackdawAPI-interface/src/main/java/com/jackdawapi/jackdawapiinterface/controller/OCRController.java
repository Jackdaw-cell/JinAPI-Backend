package com.jackdawapi.jackdawapiinterface.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jackdawapi.jackdawapiinterface.annotation.LockCheck;
import com.jackdawapi.jackdawapiinterface.annotation.SourceCheck;
import com.jackdawapi.jackdawapiinterface.model.msmBodyVO;
import com.jackdawapi.jackdawapiinterface.service.MsmService;
import com.jackdawapi.jackdawapiinterface.util.RandomNumberUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Jackdaw
 */
@RestController
@RequestMapping("/ocr")
public class OCRController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    //发送短信验证码
    @LockCheck(lockId = "")
    @SourceCheck
    @PostMapping("/getText")
    @SentinelResource(value="getUsernameByGet",fallback="fallback")
    public String code(@RequestBody msmBodyVO msm) throws TesseractException {
            ITesseract instance = new Tesseract();
            // 指定训练数据集合的路径
            instance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            // 指定为中文识别
            instance.setLanguage("chi_sim");

            // 指定识别图片
            File imgDir = new File("C:\\Users\\30893\\Pictures\\test\\Snipaste_2023-07-10_23-45-15.png");
            long startTime = System.currentTimeMillis();
            String ocrResult = instance.doOCR(imgDir);

            // 输出识别结果
            System.out.println("OCR Result: \n" + ocrResult + "\n 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
            return ocrResult;
    }

    /**
     * 降级处理
     */
    public String fallback(@RequestParam("number") String number, @RequestHeader Map<String, String> headers){
        return null;
    }
}

