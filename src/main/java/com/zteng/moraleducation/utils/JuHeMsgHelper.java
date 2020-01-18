/**
 *
 */
package com.zteng.moraleducation.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//import net.sf.json.JSONObject;

/**
 * <p>Title: JuHeMsgUtil.java</p>
 * <p>Description:聚合数据用于发送短信的工具类（www.juhe.cn） </p>
 * @author dragon
 * @date 2018年11月9日
 * @version 1.0
 */

public class JuHeMsgHelper {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY = "b2bd3cd761fe8d5d30ab03dcec1d9f39";
    //配置短信模板id
    public static final String TPL_ID = "99421";

    //发送短信
    @SuppressWarnings("unchecked")
    public static void sendMsg(String telephone, String checkCode) throws UnsupportedEncodingException {
        String result = null;
        String url = "http://v.juhe.cn/sms/send";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("mobile", telephone);//接收短信的手机号码
        params.put("tpl_id", TPL_ID);//短信模板ID，请参考个人中心短信模板设置
        String tpl_value = URLEncoder.encode("#code#=" + checkCode, "UTF-8");
        params.put("tpl_value", tpl_value);//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
        params.put("key", APPKEY);//应用APPKEY(应用详细页查询)
        // params.put("dtype","json");//返回数据的格式,xml或json，默认json

        try {
            result = net(url, params, "GET");
            // JSONObject object = JSONObject.fromObject(result);
            JSONObject object = JSON.parseObject(result);
            if (object.getIntValue("error_code") == 0) {
                System.out.println(object.get("result"));
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.发送短信验证码
    public static boolean getRequest2(String phoneNum, String text) throws UnsupportedEncodingException {
        String result = null;
        String url = "http://v.juhe.cn/sms/send";//请求接口地址
        // #code#=1234&#company#=聚合数据
        Map params = new HashMap();//请求参数
        params.put("mobile", phoneNum);//接收短信的手机号码
        params.put("tpl_id", TPL_ID);//短信模板ID，请参考个人中心短信模板设置
        String tpl_value = URLEncoder.encode("#code#=" + text, "UTF-8");
        params.put("tpl_value", tpl_value);//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>        params.put("key", APPKEY);//应用APPKEY(应用详细页查询)
        params.put("key", APPKEY);//应用APPKEY(应用详细页查询)

//        params.put("dtype", "");//返回数据的格式,xml或json，默认json

        try {
            result = net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getInteger("error_code") == 0) {
                System.out.println(object.get("result"));
                return true;
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) throws Exception {
        getRequest2("18405814001", "445566");
    }

}
