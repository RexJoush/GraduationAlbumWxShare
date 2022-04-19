package com.nwu.service;

import com.nwu.entities.AccessTokenResponse;
import com.nwu.entities.TicketResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rex Joush
 * @time 2021.06.24 21:39
 */
@Service
public class WxDataService {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.noncestr}")
    private String noncestr;

    @Value("${wx.url}")
    private String appUrl;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取 access_token
     * @return access_token
     */
    public String getAccessToken(){

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        String accessToken = operations.get("accessToken");

        // 首先去 redis 中读取 access_token, 如果存在,直接返回
        if (accessToken != null) {
//            System.out.println("redis token");
            return accessToken;
        }
        // 否则去请求微信数据,并存储缓存

        RestTemplate restTemplate = new RestTemplate();

        AccessTokenResponse token = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret, AccessTokenResponse.class);
        System.out.println(token);
        assert token != null;

        // System.out.println("token: " + token.getAccessToken());

        // 存储 7100s
        operations.set("accessToken", token.getAccessToken(), Duration.ofSeconds(7100));
        return token.getAccessToken();

    }

    /**
     * 获取 ticket
     * @return ticket
     */
    public String getTicket(){

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        String jsapiTicket = operations.get("jsapiTicket");

        // 首先去 redis 中读取 ticket, 有则直接返回; 没有,则请求微信
        if (jsapiTicket != null) {
            // System.out.println("redis ticket");
            return jsapiTicket;
        }

        RestTemplate restTemplate = new RestTemplate();

        TicketResponse ticket = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getAccessToken() + "&type=jsapi", TicketResponse.class);

        assert ticket != null;
        if (ticket.getErrCode() == 0){
            // System.out.println("ticket: " + ticket.getTicket());
            // 微信的时间有效期是 7200s, 因此存储 7100s,之后则重新获取
            operations.set("jsapiTicket", ticket.getTicket(), Duration.ofSeconds(7100));
            return ticket.getTicket();
        } else {
            return ticket.getErrMessage();
        }
    }

    /**
     * 获取 signature
     * @param timestamp 时间戳
     * @return signature
     */
    public String getSignature(String timestamp, String url){

        String plainText = "jsapi_ticket=" + getTicket() + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;

        return DigestUtils.sha1Hex(plainText);

    }

    /**
     * 获取微信所需的值
     * @return
     */
    public Map<String, Object> getWxData(String url) {

        // ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        // operations.increment("shareVisit");

        Map<String, Object> result = new HashMap<>();

        // 完整 url
        String integrateUrl = appUrl + url;

        System.out.println("integrateUrl: " + integrateUrl);

        /*
            此处,
            微信的时间戳到秒, 微信网页 js 签名工具时间戳也是秒, 但java获取的是毫秒值
            因此微信网页 js 签名工具检测结果与此处的 sha1 计算结果不一致,如果想要一致,只需把时间戳后三位删除即可一致
            但经我本人实测, 虽然与检测工具不一致, 依然可以获取到权限,即不影响业务.

            所以, 此处获取的时间戳 /1000 是可省的
         */
        long timestamp = System.currentTimeMillis() / 1000;

//        long timestamp = System.currentTimeMillis();
        System.out.println("timestamp: " + timestamp);

        String signature = getSignature(String.valueOf(timestamp), integrateUrl);


        result.put("appId", appid);
        result.put("timestamp", timestamp);
        result.put("nonceStr", noncestr);
        result.put("signature", signature);

        return result;
    }


}
