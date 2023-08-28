package com.jackdawapi.jackdawapiinterface.service;

import com.jackdawapi.jackdawapiinterface.model.msmBodyVO;

import java.util.Map;

/**
 * @author Jackdaw
 */
public interface MsmService {
    //发送验证码
    boolean send(Map<String, Object> param, msmBodyVO phone);
}

