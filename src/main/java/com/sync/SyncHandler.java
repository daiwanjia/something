package com.sync;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Pair;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author：marcdai
 * @date：2024/11/27
 */
public class SyncHandler {
    /**
     * 手动添加同步关系，匹配下游镜像
     */

    static String Cookie = "access_token=eyJraWQiOiI4YzVjNWE5N2M5Mzg2YTgxYTY0YjdlZDU0YWY0MGM4NTVhODY5YzM2IiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiJhZG1pbiIsInN1YiI6ImFkbWluQHl1ZnVpZC5jb20iLCJhY2NvdW50X3R5cGUiOiJ1c2VyQWNjb3VudCIsInVzZXJfaWQiOiJ1cy03Y2U4MTZlY2Q0YWE0ZDY1OTdmNjc3ZTM1YzRjZTJmMCIsInBlcm1pc3Npb25zIjpbImFkbWluIl0sInRudF9pZCI6InRuLWVkN2Y0ZTE4NWI4ZTQ1OTg5ODE1MzE3NTY0YjE5MDlkIiwic2NvcGUiOiJhZG1pbiIsImlzcyI6ImFkbWluIiwiZXhwIjoxNzMyNzc1NDQ5LCJpYXQiOjE3MzI3NzQ4NDksImp0aSI6IjQzNjQwNTE0LWRmMTEtNDUyMy05YThlLTI0YWMwZjUwMWE2MSJ9.KX1Ks8EsoRDB98__YdPG8tCcMWNMenex5Dkgq6zqnLFRJrt4uqIPK7xUxpGZvVEBT0yOEnchSAcUGedCZGdCh_eCQ9fpHyKsItEDo5cBYFJmoAdtfmEE-yw2t1P3u9QFWHjCR2VIJfAQFM41UF_ZcX4E58HAVzP2z-Oki77Yr0IqcKSalxnD2lnH51xxhUGPWkop1yBbwqePoeP4Ewex289bMyu0kzEjety0zzKUhzcwnh5D-3dg_dKgly1eRlo3wCdq1LIYl5EpL3jbC0_1HEGyH7MPq3ZKVdZqS8icuA1ACr05muFBIFK0CVrXBTeNAve42I0RR9qUayyiQZfnAw; refresh_token=eyJraWQiOiI4YzVjNWE5N2M5Mzg2YTgxYTY0YjdlZDU0YWY0MGM4NTVhODY5YzM2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhZG1pbkB5dWZ1aWQuY29tIiwiYWNjb3VudF90eXBlIjoidXNlckFjY291bnQiLCJpc3MiOiJhZG1pbiIsInJldm9jYWJsZSI6dHJ1ZSwiYXVkIjoiYWRtaW4iLCJ1c2VyX2lkIjoidXMtN2NlODE2ZWNkNGFhNGQ2NTk3ZjY3N2UzNWM0Y2UyZjAiLCJwZXJtaXNzaW9ucyI6WyJhZG1pbiJdLCJ0bnRfaWQiOiJ0bi1lZDdmNGUxODViOGU0NTk4OTgxNTMxNzU2NGIxOTA5ZCIsInNjb3BlIjoiYWRtaW4iLCJleHAiOjE3MzI4MTA4NDksImlhdCI6MTczMjc3NDg0OSwianRpIjoiNmY3MWRjNjAtNjk1ZS00NjAzLTk1N2MtNzM5YTNjN2ZmY2Q1In0.a74Lu0JpoFABiX5uavxyvI3Sc_ECb92cfMuIWuPgb3iZbgm_WKlvZcflE01SYknUl4vnjnnWWDwUAVOZ5u7-W5qhrOXpmIF7wSNZK_c7vb-VxY7YNqqDdo9mPJU47plVKAjI1_RBrcsW8czpezJmOCJh92htUCTPsgQXhVTFZTQGUdow_FC9evyjwq4Ul3DJVTBiC1A3VrYfEDUb8__z_6inQh3p3zHSSOce-UTUlCAB90h_cnZ1mDBHNKhMVch_FVPK0KNCQbIQzrlu8-x5ueueBzLcOt3tif9zMJjRYCSv3vLsCYjnyt2HBaRz6ZsAYfZFTvddqa7qRzeDiDXUbg; lcm_access_token=eyJraWQiOiI4YzVjNWE5N2M5Mzg2YTgxYTY0YjdlZDU0YWY0MGM4NTVhODY5YzM2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhZG1pbkB5dWZ1aWQuY29tIiwiYXVkIjoiYWRtaW4iLCJhY2NvdW50X3R5cGUiOiJ1c2VyQWNjb3VudCIsInVzZXJfaWQiOiJ1cy03Y2U4MTZlY2Q0YWE0ZDY1OTdmNjc3ZTM1YzRjZTJmMCIsInBlcm1pc3Npb25zIjpbImFkbWluIl0sInRudF9pZCI6InRuLWVkN2Y0ZTE4NWI4ZTQ1OTg5ODE1MzE3NTY0YjE5MDlkIiwic2NvcGUiOiJhZG1pbiIsImlzcyI6ImFkbWluIiwicmV2b2NhYmxlIjp0cnVlLCJleHAiOjE3MzI3NzU0NzksImlhdCI6MTczMjc3NDg3OSwianRpIjoiZjQyM2FjOTMtNzdiMC00NzdhLTg5NjMtYWY2OTM5YTcxODljIn0.XGoquC9xyLJ14luBRIcSuTgC-8YitsLmi2PZtAo2Cj_opKpSXZ3tdvwS0iiNqq-rkTCOWkHE5GSEzU0DaZAvDotrv7N2ZzNuKNy4mIg5zus9DmH77GqBIuVJZvgs1ksGZSirxMwkyAxVc4-weYVw5jL2dbczvZ0H74rFa6-DerPcPrZMx0wj4U4G6v73NlPbjxakQuImvYqW-VVHHBrixEr9j9Kut0FZEvxs_81LVwSFkvwkhSpe2DlhKiMWr_4YXT7WsFBT703qkIfsAqQ6TbPOlqB_9KArRP38iAoA_MsSj4HxKhoB9QVvjlpuTf3Xwy4RPV9ENmZ9hEMIR-JbAA";
    static String baseUrl = "https://idaas-admin.coscoshipping.com/gateway/api/v1/tn-ed7f4e185b8e45989815317564b1909d";
    //同步中下游的镜像resourceId:rc-xxx
    static String targetResourceId = "rc-b549ac02607b4afd90d952441c09bc76";
    //同步应用实例id: ai-xxx
    static String appInstanceId = "ai-832ca947169b4d378d8341396f23960a";
    //同步中同步关系connectionId，打开同步预览，浏览器链接可以找到cn-xxx
    static String connectionId = "cn-89997c00e64a49289939e5db3f3c7f7f";

    /**
     * 建立同步关系
     */
    @Test
    public void syncConnectionInfo(){
        int total = 0;
        String query = "status=CONNECTED_ERROR&operation=UPDATE";

        for (int i = 0; i < 10; i++) {
            List<Pair<String, JSONObject>> sourceImgList = getSourceImgList(query, i, 100);
            for (Pair<String, JSONObject> pair : sourceImgList) {
                total++;
                JSONObject o = pair.getValue();
                //此处修改查询条件和主键字段
                String desc = ((JSONObject) o.getJSONArray("operationDescription").get(0)).getStr("description");
                if(desc != null && desc.startsWith("405 Method Not Allowed")){
                    String username = o.getJSONObject("sourceData").getJSONObject("data").getStr("staffCode");
                    //getMatchImg
                    String matchImgId = getMatchImg(username);
                    //match
                    match(pair.getKey(), matchImgId);
                    Console.log("match complete[{}] sourceId={}, target={}", total, pair.getKey(), username);
                }else {
                    Console.log("[{}] ignore this data: {}", total, o.getStr("id"));
                }
            }
        }
    }

    /**
     * 手动同步
     */
    @Test
    public void sync(){
        String query = "status=UNCONNECTED&operation=UPDATE";
        for (int i = 0; i < 20; i++) {
            List<Pair<String, JSONObject>> list = getSourceImgList(query, i, 100);
            String[] idList = (String[]) list.stream().map(Pair::getKey).toArray();
            sync(idList);
            Console.log("sync complete[{}] ", i, Arrays.toString(idList));
        }
    }


    /**
     * 同步预览查询列表，获取列表
     * @param page
     * @param size
     * @return
     */
    public static List<Pair<String, JSONObject>> getSourceImgList(String query, int page, int size){
        String body = HttpUtil.createGet(String.format("%s/connections/%s/infos?%s&page=%s&size=%s", baseUrl, connectionId, query, page, size))
                .header("Cookie", Cookie)
                .execute().body();
        JSONArray data = JSONUtil.parseObj(body).getJSONArray("data");
        List<Pair<String, JSONObject>> list = new ArrayList<>();
        if(!data.isEmpty()){
            for (int i = 0; i < data.size(); i++) {
                int count = page * size + i + 1;
                JSONObject o = (JSONObject) data.get(i);
                String sourceId = o.getStr("sourceDataId");
                list.add(new Pair<>(sourceId, o));
                Console.log("getSourceImgList[{}] sourceId={}", count, sourceId);
            }
        }
        return list;
    }

    /**
     * 根据匹配的主键搜索下游镜像，结果不唯一返回null
     * @param uniqueId
     * @return
     */
    public static String getMatchImg(String uniqueId){
        String body = HttpUtil.createGet(String.format("%s/resources/%s/images/search?value=%s", baseUrl, targetResourceId, uniqueId))
                .header("Cookie", Cookie)
                .execute().body();
        JSONArray data = JSONUtil.parseObj(body).getJSONArray("data");
        if(data == null || data.size() != 1){
            //log
            Console.log("find not unique, ignore this id: {}", uniqueId);
            return null;
        }
        return ((JSONObject) data.get(0)).getStr("id");
    }

    /**
     * 手动匹配，上下游镜像创建同步关系
     * @param sourceImg
     * @param targetImg
     */
    public static void match(String sourceImg, String targetImg){
        if(targetImg == null){
            return;
        }
        HttpResponse response = HttpUtil.createPost(String.format("%s/syncconnections/%s/connections/%s/infos/%s/find", baseUrl, appInstanceId, connectionId, sourceImg))
                .header("Cookie", Cookie)
                .body(JSONUtil.toJsonStr(Collections.singletonMap("targetDataId", targetImg)))
                .execute();
        if(!response.isOk()){
            Console.log("[{}]match error: {} : {}", sourceImg, response.getStatus(), response.body());
        }
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 手动同步，传入左侧上游镜像id数组
     * @param sourceImgIds
     */
    public static void sync(String[] sourceImgIds){
        JSONObject req = new JSONObject()
                .set("type", "SYNC")
                .set("range", "SELECTED")
                .set("lastSyncTime", 1732633201344L)
                .set("imageIds", sourceImgIds);
        HttpResponse response = HttpUtil.createPost(String.format("%s/syncconnections/%s/connections/%s/sync", baseUrl, appInstanceId, connectionId))
                .header("Cookie", Cookie)
                .body(req.toString())
                .execute();
        if(!response.isOk()){
            Console.log("[{}]sync error: {} : {}", Arrays.toString(sourceImgIds), response.getStatus(), response.body());
        }
    }
}
