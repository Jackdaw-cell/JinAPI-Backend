package com.jackdawapi.jackdawapiinterface.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jackdawapi.jackdawapiinterface.annotation.LockCheck;
import com.jackdawapi.jackdawapiinterface.annotation.SourceCheck;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKey;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKeyImpl;
import com.jackdawapi.jackdawapiinterface.util.TransApi;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yupi.yucongming.dev.utils.SignUtils.genSign;

/**
 * @author Jackdaw
 */
@RestController
@RequestMapping("/translate")
public class TranslateController {

    private static final LockByKey<String> lockByKey = new LockByKeyImpl<>();
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    String APP_ID = "20230915001817930";
    String SECURITY_KEY = "y3SpTvTUp_JEXpRjuynH";

    @LockCheck(lockId = "")
    @SourceCheck
    @GetMapping(value = "/query/en" , produces = "application/json;charset=UTF-8")
    @SentinelResource(value="getUsernameByGet",fallback="fallback")
    public String translateENByGet(@RequestParam("query") String query, @RequestHeader Map<String, String> headers) throws  InterruptedException {
            try{
                 TransApi api = new TransApi(APP_ID, SECURITY_KEY);
                String input = api.getTransResult(query, "auto", "en");
                String pattern = "\"dst\":\"(.*?)\"";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(input);
                if (m.find()) {
                    String result = m.group(1);
                    return result;
                }
                return "error:返回错误";
            }catch (Exception e){
                 throw new InterruptedException();
            }
    }

    @LockCheck(lockId = "")
    @SourceCheck
    @GetMapping(value = "/query/zh" , produces = "application/json;charset=UTF-8")
    @SentinelResource(value="getUsernameByGet",fallback="fallback")
    public String translateZHByGet(@RequestParam("query") String query, @RequestHeader Map<String, String> headers) throws  InterruptedException {
        try{
            TransApi api = new TransApi(APP_ID, SECURITY_KEY);
            String input = api.getTransResult(query, "auto", "zh");
            String pattern = "\"dst\":\"(.*?)\"";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(input);
            System.out.println(m);
            if (m.find()) {
                String result = m.group(1);
                return result;
            }
            return "error:返回错误";
        }catch (Exception e){
            throw new InterruptedException();
        }
    }

    @LockCheck(lockId = "")
    @SourceCheck
    @GetMapping(value = "/query/yue" , produces = "application/json;charset=UTF-8")
    @SentinelResource(value="getUsernameByGet",fallback="fallback")
    public String translateYUEByGet(@RequestParam("query") String query, @RequestHeader Map<String, String> headers) throws  InterruptedException {
        try{
            TransApi api = new TransApi(APP_ID, SECURITY_KEY);
            String input = api.getTransResult(query, "auto", "yue");
            String pattern = "\"dst\":\"(.*?)\"";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(input);
            if (m.find()) {
                String result = m.group(1);
                return result;
            }
            return "error:返回错误";
        }catch (Exception e){
            throw new InterruptedException();
        }
    }
    /**
     * 降级处理
     */
    public String fallback(@RequestParam("number") String number, @RequestHeader Map<String, String> headers){
        return null;
    }
}
