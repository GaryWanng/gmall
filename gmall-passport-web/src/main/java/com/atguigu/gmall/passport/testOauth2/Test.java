package com.atguigu.gmall.passport.testOauth2;

import com.atguigu.gmall.util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String access_token_url = "https://api.weibo.com/oauth2/access_token?client_id= 350752890&client_secret=388b3300158cee6b4ed199cdb50ccff5&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8090/vlogin&code=2aab5d5987c0ba3c72d2702e4b8f1872";

        Map<String,String> map = new HashMap<String,String>();
        map.put("client_id","350752890");
        map.put("client_secret","388b3300158cee6b4ed199cdb50ccff5");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://passport.gmall.com:8090/vlogin");
        map.put("code","2aab5d5987c0ba3c72d2702e4b8f1872");
        String json = HttpclientUtil.doPost("https://api.weibo.com/oauth2/access_token", map);

        System.out.println(json);
    }
}
