package com.jackdawapi.jackdawapiinterface.controller;

import com.jackdawapi.jackdawapiinterface.annotation.LockCheck;
import com.jackdawapi.jackdawapiinterface.annotation.SourceCheck;
import com.jackdawapi.jackdawapiinterface.model.teacherVO;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKey;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKeyImpl;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author Jackdaw
 */
@RestController
@RequestMapping("/user")
public class NameController {

    private static final LockByKey<String> lockByKey = new LockByKeyImpl<>();

    @LockCheck(lockId = "")
    @SourceCheck
    @GetMapping(value = "/number" , produces = "application/json;charset=UTF-8")
    public String getUsernameByGet(@RequestParam("number") String number, @RequestHeader Map<String, String> headers) throws  InterruptedException {
            try{
                 number =new String(number.getBytes("ISO-8859-1"), "UTF-8");
                 Thread.sleep(8000);
                 return "GET NUMBER 是：" + number;
            }catch (Exception e){
                 throw new InterruptedException();
            }
    }
}
