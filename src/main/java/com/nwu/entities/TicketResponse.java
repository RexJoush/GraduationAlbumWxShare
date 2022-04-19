package com.nwu.entities;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Rex Joush
 * @time 2021.06.24 22:02
 */

@Data
public class TicketResponse {

    @JsonProperty("err_code")
    private int errCode;            // 错误代码

    @JsonProperty("err_message")
    private String errMessage;      // 错误信息

    @JsonProperty("ticket")
    private String ticket;          // ticket 值

    @JsonProperty("expires_in")
    private int expiresIn;          // 过期时间
}
