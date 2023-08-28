package com.jackdawapi.jackdawapiinterface.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jackdawapi.jackdawapiinterface.model.msmBodyVO;
import com.jackdawapi.jackdawapiinterface.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Jackdaw
 */
@Service
public class MsmServiceImpl implements MsmService {
    /**
     * 发送验证码
     * @param param     验证码
     * @param msm     消息对象
     * @return
     */
    @SuppressWarnings("AlibabaAvoidCommentBehindStatement")
    @Override
    public boolean send(Map<String, Object> param, msmBodyVO msm) {

        if(StringUtils.isEmpty(msm.getPhoneNumbers())) {
            return false;
        }

        //default 地域节点，默认就好，后面是阿里云的id和秘钥（这里记得去阿里云复制自己的id和秘钥哦）
        DefaultProfile profile = DefaultProfile.getProfile(msm.getRegionId(), msm.getAccessKey(), msm.getSecretKey());

        IAcsClient client = new DefaultAcsClient(profile);

        //这里不能修改
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //手机号
        request.putQueryParameter("PhoneNumbers", msm.getPhoneNumbers());
        //申请阿里云 签名名称（暂时用阿里云测试的，自己还不能注册签名）
        request.putQueryParameter("SignName", msm.getSignName());
        //申请阿里云 模板code（用的也是阿里云测试的）
        request.putQueryParameter("TemplateCode", msm.getTemplateCode());
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

