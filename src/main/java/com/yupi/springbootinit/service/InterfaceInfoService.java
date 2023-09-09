package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jackdawapi.jackdawapicommon.model.entity.InterfaceInfo;

/**
* @author Jackdaw
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-05-26 21:28:47
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId, long userId);
}
