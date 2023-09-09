package com.yupi.springbootinit.service.impl.inner;


import com.jackdawapi.jackdawapicommon.service.InnerUserInterfaceInfoService;
import com.yupi.springbootinit.service.InterfaceInfoService;
import com.yupi.springbootinit.service.UserInterfaceInfoService;
import com.yupi.springbootinit.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Jackdaw
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

//    @Resource
//    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Override
    @Transactional
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userService.invokeCount(interfaceInfoId, userId) && interfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
