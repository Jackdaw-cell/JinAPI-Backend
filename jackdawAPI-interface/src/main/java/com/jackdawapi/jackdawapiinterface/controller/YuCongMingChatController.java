package com.jackdawapi.jackdawapiinterface.controller;

import com.jackdawapi.jackdawapiinterface.annotation.LockCheck;
import com.jackdawapi.jackdawapiinterface.annotation.SourceCheck;
import com.jackdawapi.jackdawapiinterface.model.chatBodyVO;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Jackdaw
 */
@RestController
@RequestMapping("/yucongming")
public class YuCongMingChatController {

    @LockCheck(lockId = "")
    @SourceCheck
    @PostMapping("/chat")
    public String chat(@RequestBody chatBodyVO chatBody,  @RequestHeader Map<String, String> headers){
            try {
                    String accessKey = chatBody.getAccessKey();
                    String secretKey = chatBody.getSecretKey();
                    Long modelId = chatBody.getModelId();
                    String message = chatBody.getMessage();
                    YuCongMingClient client = new YuCongMingClient(accessKey, secretKey);
                    DevChatRequest devChatRequest = new DevChatRequest();
                    devChatRequest.setModelId(modelId);
                    devChatRequest.setMessage(message);
                    BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
                    return response.getData().getContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
}
