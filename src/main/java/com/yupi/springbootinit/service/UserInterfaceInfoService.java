package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jackdawapi.jackdawapicommon.model.entity.UserInterfaceInfo;

/**
* @author Jackdaw
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-05-26 21:28:47
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userinterfaceInfo, boolean add);

    /**
     * 调用次数加一
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);
}
