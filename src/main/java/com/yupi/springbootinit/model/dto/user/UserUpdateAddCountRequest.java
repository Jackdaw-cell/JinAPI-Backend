package com.yupi.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 */
@Data
public class UserUpdateAddCountRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 次数
     */
    private Integer count;

    private static final long serialVersionUID = 1L;
}