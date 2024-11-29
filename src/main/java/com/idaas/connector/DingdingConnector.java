package com.idaas.connector;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：marcdai
 * @date：2024/11/19
 */
public class DingdingConnector {
    String appkey = "dingiwlnsdv9v7hvxrby";
    String appsecret = "MmU33HtKXUc02Xj8w91crZ9IZevHJj4mB11xbWCa8mii9d_SDhFtqknMYUB_3IKI";

    protected String accessToken = "9916556eef88300a8fadbe2cd8d6fca8";

    @Test
    public void gettoken() {
        String url = "https://oapi.dingtalk.com/gettoken?app";
        Map<String, String> map = new HashMap<>();
        map.put("appkey", appkey);
        map.put("appsecret", appsecret);
        String getTokenUrl = UrlBuilder.of(url).addQuery("appkey", appkey)
                .addQuery("appsecret", appsecret)
                .build();
        String resp = HttpUtil.get(getTokenUrl);
        System.out.println(resp);
        accessToken = JSONUtil.parseObj(resp).get("access_token").toString();
    }

    @Test
    public void getDepts() {
        String url = "https://oapi.dingtalk.com/department/list";
        String resp = HttpUtil.get(UrlBuilder.of(url).addQuery("access_token", accessToken).addQuery("fetch_child", true).build());
        System.out.println(resp);
    }

    @Test
    public void getUsers() {
        int deptId = 401698322;
        String url = "https://oapi.dingtalk.com/user/listbypage";
        String resp = HttpUtil.get(UrlBuilder.of(url)
                .addQuery("access_token", accessToken)
                .addQuery("department_id", deptId)
                .addQuery("size", 1)
                .addQuery("offset", 0)
                .build());
        System.out.println(resp);
        JSONObject userinfo = (JSONObject) JSONUtil.parseObj(resp).getJSONArray("userlist").get(0);
        String avatar = userinfo.getStr("avatar");
        System.out.println("old avatar: " + avatar);
        System.out.println("old avatar len: " + avatar.length());
        String url2 = "https://oapi.dingtalk.com/topapi/v2/user/list";
        String resp2 = HttpUtil.get(UrlBuilder.of(url2)
                .addQuery("access_token", accessToken)
                .addQuery("dept_id", deptId)
                .addQuery("size", 1)
                .addQuery("cursor", 0)
                .build());
        // copy "department" from "dept_id_list"
        System.out.println(resp2);
        JSONObject userinfo2 = (JSONObject) JSONUtil.parseObj(resp2).getJSONObject("result").getJSONArray("list").get(0);
        String avatar2 = userinfo2.getStr("avatar");
        System.out.println("new avatar: " + avatar2);
        System.out.println("new avatar len: " + avatar2.length());
        System.out.println("equals: " + avatar.equals(avatar2));
    }

    @Test
    public void getUser2() {
        int deptId = 401698322;
        String url2 = "https://oapi.dingtalk.com/topapi/v2/user/list";
        String resp2 = HttpUtil.get(UrlBuilder.of(url2)
                .addQuery("access_token", accessToken)
                .addQuery("dept_id", deptId)
                .addQuery("size", 2)
                .addQuery("cursor", "0")
                .build());
        // copy "department" from "dept_id_list"
        System.out.println(resp2);
        JSONObject userinfo2 = (JSONObject) JSONUtil.parseObj(resp2).getJSONObject("result").getJSONArray("list").get(0);
        String avatar2 = userinfo2.getStr("avatar");
        System.out.println(avatar2);
    }
}