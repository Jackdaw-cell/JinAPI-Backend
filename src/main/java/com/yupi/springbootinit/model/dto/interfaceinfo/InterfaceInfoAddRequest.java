package com.yupi.springbootinit.model.dto.interfaceinfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应体
     */
    private String responseBody;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 测试请求参数
     */
    private String testRequestParams;

    /**
     * 测试响应结果
     */
    private String testResponse;
    /**
     * 请求类型
     */
    private String method;

    /**
     * 0-长连接，1-短链接
     */
    private Integer connectType;

    /**
     * 调用次数
     */
    private Integer count;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}