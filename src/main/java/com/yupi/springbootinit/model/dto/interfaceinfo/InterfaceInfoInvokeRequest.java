package com.yupi.springbootinit.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 请求参数
     */
    private String userRequestParams;

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