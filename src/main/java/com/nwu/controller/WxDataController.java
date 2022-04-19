package com.nwu.controller;

import com.nwu.service.WxDataService;
import com.nwu.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Rex Joush
 * @time 2021.06.24 21:38
 */

@RestController
public class WxDataController {

    @Resource
    private WxDataService wxDataService;

    @GetMapping("/getWxData")
    public String getWxData(@RequestParam("url") String url){

//        System.out.println(url);

        Map<String, Object> wxData = wxDataService.getWxData(url);

//        return Result.ok("获取微信数据成功");
        return Result.ok("获取微信数据成功", wxData);

    }

}
