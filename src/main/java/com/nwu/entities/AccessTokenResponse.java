package com.nwu.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Rex Joush
 * @time 2021.06.24 21:57
 */

@Data
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;     // token 值

    @JsonProperty("expires_in")
    private int expiresIn;          // 过期时间


}
