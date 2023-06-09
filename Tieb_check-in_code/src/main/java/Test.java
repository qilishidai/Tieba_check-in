

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;


import java.io.*;
import java.net.*;



/**
 * get请求测试
 * @author liujilong
 * @since 2019-7-18 10:26:49
 */
public class Test {
    private static final Log log = LogFactory.get();

    public static void main(String[] args) throws Exception {
        String url = "https://sctapi.ftqq.com/SCT16463TykjnvVZHH9zwcrpu93jedzCc.send";
        String title = "你在干什么";
        String message = "你今天吃饭了吗";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            String data = "title=" + URLEncoder.encode(title, "UTF-8") +
                          "&desp=" + URLEncoder.encode(message, "UTF-8");
            OutputStream os = con.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }


	}


}

