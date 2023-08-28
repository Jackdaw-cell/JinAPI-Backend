package com.jackdawapi.jackdawapisdk.model.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @sine 2023-04-08
 */
@Data
@AllArgsConstructor
@Getter
@Setter
public class ChatResponse {

    private int code;

    /**
     * 问题消耗tokens
     */
    private long questionTokens = 0;

    private String message;

    public ChatResponse(){}
}
