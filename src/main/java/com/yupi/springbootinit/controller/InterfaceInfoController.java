package com.yupi.springbootinit.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.jackdawapi.jackdawapicommon.model.entity.InterfaceInfo;
import com.jackdawapi.jackdawapisdk.model.OpenAI.ChatRequest;
import com.jackdawapi.jackdawapisdk.model.OpenAI.ChatResponse;
import com.jackdawapi.jackdawapicommon.model.entity.User;
import com.jackdawapi.jackdawapisdk.client.JackdawApiClient;
import com.jackdawapi.jackdawapisdk.client.JackdawOpenAiClient;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.jackdawapi.jackdawapisdk.common.*;
import com.jackdawapi.jackdawapisdk.constant.CommonConstant;
import com.jackdawapi.jackdawapisdk.exception.BusinessException;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yupi.springbootinit.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.yupi.springbootinit.model.enums.InterfaceInfoStatusEnum;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 *
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private RestTemplate template;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private JackdawApiClient jackdawApiClient;

    @Resource
    private JackdawOpenAiClient jackdawOpenAiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        interfaceInfo.getTestRequestParams();
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        Integer connectType = interfaceInfoQueryRequest.getConnectType();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        interfaceInfoQuery.setConnectType(connectType);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 发布
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断该接口是否可以调用
        // 业务存在两个User，一个是接口开放平台的User，一个是接口要用到的User，这里要用到接口的User来调用接口的业务
        String requestParams = oldInterfaceInfo.getTestRequestParams();
        String bodyJson = JSONUtil.toJsonStr(requestParams);
        String url = oldInterfaceInfo.getUrl();
        BaseResponse<Object> testResult = new BaseResponse<Object>();
        if(oldInterfaceInfo.getMethod().equals("POST")){
            testResult = jackdawApiClient.postRequest(bodyJson, url);
        }else if (oldInterfaceInfo.getMethod().equals("GET")){
            testResult = jackdawApiClient.getRequest(bodyJson, url);
        }else if(oldInterfaceInfo.getMethod().equals("Long")){
            testResult.setCode(0);
        }
        if (testResult.getCode()==0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean updateResult = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(updateResult);
    }


    /**
     * 下线
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                      HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 短连接Post请求调用【开放平台】
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invokeByPost")
    public BaseResponse<Object> invokeInterfaceInfoByPost(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        // 取出用户的AK和SK，发送请求时带上用户自己的AK和SK
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // 接口调用靠的是这个 YuAoiClient（不管是apiCenter还是用户自己的系统）
        JackdawApiClient tempJackdawApiClient = new JackdawApiClient(accessKey, secretKey);
        String bodyJson = JSONUtil.toJsonStr(userRequestParams);
        String url = oldInterfaceInfo.getUrl();
        return tempJackdawApiClient.postRequest(bodyJson, url);
    }

    /**
     * Get请求调用【开放平台】
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invokeByGet")
    public BaseResponse<Object> invokeInterfaceInfoByGet(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        // 取出用户的AK和SK，发送请求时带上用户自己的AK和SK
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // 接口调用靠的是这个 YuAoiClient（不管是apiCenter还是用户自己的系统）
        JackdawApiClient tempClient = new JackdawApiClient(accessKey, secretKey);
        String bodyJson = JSONUtil.toJsonStr(userRequestParams);
        String url = oldInterfaceInfo.getUrl();
        return tempClient.getRequest(bodyJson, url);
    }

    /**
     * 创建SSE连接【开放平台】
     */
    @GetMapping("/invokeByCreateSse")
    public SseEmitter invokeInterfaceInfoByCreateSse(@RequestHeader Map<String, String> headers) {
        String loginUserId = headers.get("uid");
        if (StrUtil.isBlank(loginUserId)) {
            throw new BaseException(CommonError.SYS_ERROR);
        }
        try{
            // TODO:改为创建者模式，一层一层地Build
            JackdawOpenAiClient tempTackdawOpenAiClient = new JackdawOpenAiClient("","https://api.openai.com/","127.0.0.1",7890);
            return tempTackdawOpenAiClient.createConnect();
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建SSE客户端失败");
        }
    }

    /**
     * 对话【开放平台】
     * apiKey在chatRequest里面
     * 备用：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
     * @return
     */
    @PostMapping("/invokeBySseChat")
    public ChatResponse invokeInterfaceInfoBySseChat(@RequestHeader Map<String, String> headers,
                                                     @RequestBody ChatRequest chatRequest) {
        String loginUserId = headers.get("uid");
        if (StrUtil.isBlank(loginUserId)) {
            throw new BaseException(CommonError.SYS_ERROR);
        }
        //接口调用靠的是这个 YuAoiClient（不管是apiCenter还是用户自己的系统）
        String apiKey = chatRequest.getApiKey();
        JackdawOpenAiClient tempJackdawOpenAiClient = new JackdawOpenAiClient(apiKey,"https://api.openai.com/","127.0.0.1",7890);
        try{
            return tempJackdawOpenAiClient.sseChat( chatRequest);
        }catch(Exception e){
            return new ChatResponse(512,0,"创建对话失败");
        }
    }


    /**
     * Get请求调用【SDK】
     * @return
     */
    @PostMapping("/invokeByGetSDK")
    public BaseResponse<Object> invokeInterfaceInfoByGetSDK() {
        // 请求参数/请求体的JSON字符串
        String userRequestParams="{\"number\":\"114514\"}";
        // 配置文件取出自己的AK和SK
        String bodyJson = JSONUtil.toJsonStr(userRequestParams);
        // url字符串
        String url = "/user/number";
        return jackdawApiClient.getRequest(bodyJson, url);
    }

    /**
     * 短连接Post请求调用例 1:[访问鱼聪明api]【SDK】
     * @return
     */
    @PostMapping("/invokeYvCongMingByPostSDK")
    public BaseResponse<Object> invokeInterfaceYvCongMingInfoByPostSDK() {
        // 请求参数/请求体的JSON字符串
        String userRequestParams="{\"accessKey\":\"m3myozewahwadx4gvdjxnjrmm3c3k4f0\",\"secretKey\":\"6xw04z98gjuq1gv5qhtnsr951pxmitou\",\"modelId\":1651468516836098050,\"message\":\"浮夸\"}";
        // 配置文件取出自己的AK和SK
        String bodyJson = JSONUtil.toJsonStr(userRequestParams);
        // url字符串
        String url = "/yucongming/chat";
        return jackdawApiClient.postRequest(bodyJson, url);
    }

    /**
     * 短连接Post请求调用例 2: [访问阿里云短信平台]【SDK】
     * @return
     */
    @PostMapping("/invokeAliYunMessageByPostSDK")
    public BaseResponse<Object> invokeInterfaceAliYunMessageInfoByPostSDK() {
        // 请求参数/请求体的JSON字符串
        String userRequestParams="{\"phoneNumbers\":\"18138769734\",\"signName\":\"本居铃奈庵\",\"templateCode\":\"SMS_460745222\",\"regionId\":\"default\",\"accessKey\":\"LTAI5tBdZ6AjQyhovG1b1kYF\",\"secretKey\":\"2edid9kIbwvCAs4w9IktuaIVoLO4dF\"}";
        // 配置文件取出自己的AK和SK
        String bodyJson = JSONUtil.toJsonStr(userRequestParams);
        // url字符串
        String url = "/sms/send";
        return jackdawApiClient.postRequest(bodyJson, url);
    }

    /**
     * 创建SSE连接【SDK】
     */
    @GetMapping("/invokeByCreateSseSDK")
    public SseEmitter invokeInterfaceInfoByCreateSseSDK() {
        try{
            return jackdawOpenAiClient.createConnect();
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建SSE客户端失败");
        }
    }

    /**
     * 对话【SDK】
     * apiKey在chatRequest里面
     * 备用：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
     * @return
     */
    @PostMapping("/invokeBySseChatSDK")
    public ChatResponse invokeInterfaceInfoBySseChatSDK() {
        ChatRequest chatRequest=new ChatRequest();
        chatRequest.setMsg("你知道女神异闻录吗？");
        try{
            return jackdawOpenAiClient.sseChat( chatRequest);
        }catch(Exception e){
            return new ChatResponse(512,0,"创建对话失败");
        }
    }
}
