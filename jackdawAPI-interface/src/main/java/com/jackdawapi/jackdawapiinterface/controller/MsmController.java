package com.jackdawapi.jackdawapiinterface.controller;

import com.jackdawapi.jackdawapiinterface.annotation.LockCheck;
import com.jackdawapi.jackdawapiinterface.annotation.SourceCheck;
import com.jackdawapi.jackdawapiinterface.model.chatBodyVO;
import com.jackdawapi.jackdawapiinterface.model.msmBodyVO;
import com.jackdawapi.jackdawapiinterface.service.MsmService;
import com.jackdawapi.jackdawapiinterface.util.RandomNumberUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Body;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Eric
 * @create  2022-05-22 15:12
 */
@RestController
@RequestMapping("/sms")
public class MsmController {

    @Resource
    private MsmService msmService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    //发送短信验证码
    @LockCheck(lockId = "")
    @SourceCheck
    @PostMapping("/send")
    public Boolean code(@RequestBody msmBodyVO msm, @RequestHeader Map<String, String> headers) {
            //1、从redis中获取验证码，如果获取到就直接返回
            String code = redisTemplate.opsForValue().get(msm.getPhoneNumbers());
            if(!StringUtils.isEmpty(code)) {
                return false;
            }

            //2、如果获取不到，就进行阿里云发送
            //生成验证码的随机值
            code = RandomNumberUtil.getFourBitRandom();
            Map<String,Object> param = new HashMap<>();
            param.put("code", code);

            //调用方法
            boolean isSend = msmService.send(param,msm);
            if(isSend) {
                //往redis中设置数据：key、value、过期值、过期时间单位  MINUTES代表分钟
                redisTemplate.opsForValue().set(msm.getPhoneNumbers(), code,5, TimeUnit.MINUTES);
                return true;
            } else {
                return false;
            }
    }
}

