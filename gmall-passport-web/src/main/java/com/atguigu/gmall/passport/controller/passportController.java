package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.beans.UmsMember;
import com.atguigu.gmall.passport.util.JwtUtil;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.util.HttpclientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class passportController {

    @Reference
    UserService userService;

    @RequestMapping("vlogin")
    public String vlogin(String code, HttpServletRequest request) {

        // 换取access_token
        String access_token_url = "https://api.weibo.com/oauth2/access_token?client_id=350752890&client_secret=388b3300158cee6b4ed199cdb50ccff5&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8090/vlogin&code=2aab5d5987c0ba3c72d2702e4b8f1872";

        Map<String,String> map = new HashMap<String,String>();
        map.put("client_id","350752890");
        map.put("client_secret","388b3300158cee6b4ed199cdb50ccff5");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://passport.gmall.com:8090/vlogin");
        map.put("code",code);
        String access_json = HttpclientUtil.doPost("https://api.weibo.com/oauth2/access_token", map);
        System.out.println(access_json);

        Map<String,String> map_access_json =  new HashMap<String,String>();
        Map access_map = JSON.parseObject(access_json, map_access_json.getClass());


        // 获得第三方用户数据
        String access_token = (String)access_map.get("access_token");
        String uid = (String)access_map.get("uid");// uid uidStr
        UmsMember umsMember = new UmsMember();
        umsMember = userService.isUidExists(uid);

        if(ObjectUtils.isEmpty(umsMember)){

            String show_url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;

            UmsMember umsMember1 = new UmsMember();

            String user_json = HttpclientUtil.doGet(show_url);
            Map<String,String> map_user_json = new HashMap<String,String>();
            Map user_map = JSON.parseObject(user_json, map_user_json.getClass());
            System.out.println(user_map);

            // 存入数据库
            umsMember1.setNickname((String)user_map.get("screen_name"));
            umsMember1.setUsername((String)user_map.get("name"));
            umsMember1.setSourceType("2");
            umsMember1.setSourceUid((String)user_map.get("idstr"));
            umsMember1.setCreateTime(new Date());
            umsMember1.setAccessToken(access_token);
            umsMember1.setAccessCode(code);
            umsMember = userService.addUser(umsMember1);
        }


        // 根据用户信息生成token
        String key = "atguigusso";
        String ip = request.getRemoteAddr();
        Map<String,Object> token_map = new HashMap<>();
        token_map.put("nickname",umsMember.getNickname());
        token_map.put("memberId",umsMember.getId());
        String token = JwtUtil.encode(key, token_map, ip);

        // 将生成的token和登录用户信息保存在缓存中一份
        userService.addUserCache(token,umsMember);
        return "redirect:http://search.gmall.com:8083/index?newToken="+token;
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request) {
        System.out.println("用户登录，验证用户名和密码是否正确");

        // 调用用户服务userService,验证用户名和密码
        UmsMember umsMemberFromDb = userService.login(umsMember);

        if (umsMemberFromDb == null) {
            return "fail";
        } else {
            // 根据已经登录的用户信息和，服务器密钥，和其他盐值(根据系统算法)生成一个token
            String key = "atguigusso";
            String ip = request.getRemoteAddr();
            //String ip = request.getHeader("x-forward-for");//nginx
            Map<String, Object> map = new HashMap<>();
            map.put("nickname", umsMemberFromDb.getNickname());
            map.put("memberId", umsMemberFromDb.getId());
            String token = JwtUtil.encode(key, map, ip);

            // 将生成的token和登录用户信息保存在缓存中一分
            userService.addUserCache(token,umsMemberFromDb);

            return token;
        }


    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token, String currentIp) {
        System.out.println("认证中心认证用户的token");


        String key = "atguigusso";

        String ip = currentIp;

        Map<String, Object> map = JwtUtil.decode(token, key, ip);


        Map<String,String> verifyReturn = new HashMap<>();
        if (map != null) {
            verifyReturn.put("success","success");
            verifyReturn.put("memberId",(String)map.get("memberId"));
            verifyReturn.put("nickname",(String)map.get("nickname"));
            return JSON.toJSONString(verifyReturn);
        } else {
            verifyReturn.put("success","fail");
            return JSON.toJSONString(verifyReturn);
        }


    }

    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap) {
        System.out.println("认证中心首页");

        modelMap.put("ReturnUrl", ReturnUrl);
        return "index";
    }
}
