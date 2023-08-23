package com.jackdawapi.jackdawapiinterface.service;

import com.jackdawapi.jackdawapiinterface.model.msmBodyVO;

import java.util.Map;

/**
 * @author Eric
 * @create 2022-05-22 15:08
 */
public interface MsmService {
    //发送验证码
    boolean send(Map<String, Object> param, msmBodyVO phone);
}

