package tb;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import tb.KeyValueClass;

/**
 * 签到
 *
 * @author gengwenxuan
 */
public class AutoSign {
    //推荐创建不可变静态类成员变量
    private static final Log log = LogFactory.get();
    public static final String LIKIE_URL = "http://c.tieba.baidu.com/c/f/forum/like";
    public static final String TBS_URL = "http://tieba.baidu.com/dc/common/tbs";
    public static final String SIGN_URL = "http://c.tieba.baidu.com/c/c/forum/sign";
    public static final String PUSH_PLUS_URL = "http://www.pushplus.plus/send";
    private static final String TG_PUSH_URL = "https://api.telegram.org/bot";


    public static final String FORUM_LIST = "forum_list";
    public static final String NON_GCONFORUM = "non-gconforum";
    public static final String GCONFORUM = "gconforum";

    public static final String BDUSS = "BDUSS";
    public static final String EQUAL = "=";
    public static final String EMPTY_STR = "";
    public static final String TBS = "tbs";
    public static final String TIMESTAMP = "timestamp";
    public static final String FID = "fid";
    public static final String SIGN_KEY = "tiebaclient!!!";
    public static final String UTF8 = "utf-8";
    public static final String SIGN = "sign";
    public static final String KW = "kw";
    private static final String HAS_MORE = "has_more";



    /**
     * 已签到
     */
    public static final String SIGNED = "160002";
    /**
     * 签到失败，该吧可能已被封禁
     */
    public static final String ERROR = "340006";
    /**
     * 签到成功
     */
    public static final String SUCCESS = "0";

    public AutoSign() {
    }


    public void mainHandler(String[] args) {
	
    	String bdusses1 = args[0];
    	
    	
    	String[] bdusses = bdusses1.split("#");

        if (bdusses.length == 0) {
            log.error("没有设置BDUSS");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("本次贴吧自动签到共计").append(bdusses.length).append("个用户。");
        for (int i = 0; i < bdusses.length; i++) {
            String tbs = getTbs(bdusses[i]);
            List<JSONObject> favorite = getFavorite(bdusses[i],"1");

            StringBuilder sb = new StringBuilder();
            int successCount = 0;
            int errorCount = 0;
            int signedCount = 0;
            for (JSONObject jsonObject : favorite) {
                JSONObject signJson = clientSign(tbs, jsonObject.getStr("id"), jsonObject.getStr("name"), bdusses[i]);
                if (signJson.getStr("error_code").equals(SUCCESS)) {
                    successCount += 1;
                } else if (signJson.getStr("error_code").equals(ERROR)) {
                    errorCount += 1;
                    sb.append(jsonObject.getStr("name")).append("、");
                } else if (signJson.getStr("error_code").equals(SIGNED)) {
                    signedCount += 1;
                } else {
                    errorCount += 1;
                    sb.append(jsonObject.getStr("name")).append("、");
                }
            }

            String names = sb.toString();
            if (errorCount == 0 && StrUtil.isBlank(names)) {
                stringBuilder.append("第").append(i + 1).append("个用户签到完成，共计").append(favorite.size()).append("个贴吧，成功签到")
                        .append(successCount).append("个吧，已经签过到").append(signedCount).append("个贴吧，签到失败")
                        .append(errorCount).append("个贴吧！")
                        .append("<hr/>");
            } else if (StrUtil.isNotBlank(names) && errorCount >0) {
                names = names.substring(0, names.lastIndexOf("、"));
                stringBuilder.append("第").append(i + 1).append("个用户签到完成，共计").append(favorite.size()).append("个贴吧，成功签到")
                        .append(successCount).append("个吧，已经签过到").append(signedCount).append("个贴吧，签到失败")
                        .append(errorCount).append("个贴吧！").append("签到失败名单如下：").append(names)
                        .append("。失败原因可能是该贴吧已经被封禁！")
                        .append("<hr/>")
                ;
            }
            	//消息推送
    		    if(args.length == 2) {
    		        //第一个cookie，第二个为SendKey。

    		        try {
						sever酱推送(stringBuilder.toString(), args[1]);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
    		    } else if(args.length == 4) {
    		        //第一个为发送的消息 ，String 企业ID，企业应用secret,企业应用的id;
    		    	try {
						pushMsg(stringBuilder.toString(), args[1], args[2], args[3]);
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
    		    } else {
    		    	 log.info("无效的输入推送参数。");
    		    }
    		    
				//pushMsg(stringBuilder.toString(),errorCount);

        }
    }


    private static JSONObject clientSign(String tbs, String id, String name, String bduss) {
        log.info("开始签到贴吧：" + name);
        JSONObject data = generateSignData();
        data.set(BDUSS, bduss);
        data.set(FID, id);
        data.set(KW, name);
        data.set(TBS, tbs);
        data.set(TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
        encodeData(data);
        String body = HttpRequest.post(SIGN_URL).form(data)
                .execute().body();
        body = UnicodeUtil.toString(body);
        return new JSONObject(body);
    }

    /**
     * 获取收藏列表
     *11
     * @return 收藏列表
     */
    private static List<JSONObject> getFavorite(String bduss,String pageNoValue) {
        log.info("开始获取关注贴吧");
        JSONObject params = new JSONObject();
        params.set(BDUSS, bduss);
        params.set("_client_type", "2");
        params.set("_client_id", "wappc_1534235498291_488");
        params.set("_client_version", "9.7.8.0");
        params.set("_phone_imei", "000000000000000");
        params.set("from", "1008621y");
        params.set("page_no", pageNoValue);
        params.set("page_size", "200");
        params.set("model", "MI+5");
        params.set("net_type", "1");
        params.set("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.set("vcode_tag", "11");
        encodeData(params);
        try {

            String strBody = UnicodeUtil.toString(HttpRequest.post(LIKIE_URL).form(params)
                    .execute().body());
            JSONObject body = new JSONObject(strBody);
            JSONArray nonJsonArray = new JSONArray();
            JSONArray gcoJsonArray = new JSONArray();
            List<JSONObject> finalData = new ArrayList<>();
            if (body.containsKey(FORUM_LIST)) {
                JSONObject forumJson = null;
                try {
                    forumJson = body.getJSONObject(FORUM_LIST);
                } catch (ClassCastException e) {
                    JSONArray testArray = body.getJSONArray(FORUM_LIST);
                    if (testArray.size() == 0) {
                        log.info("似乎已经到底了");
                    } else {
                        log.error("关注贴吧似乎有问题" + testArray.toString());
                    }
                }
                if (forumJson != null) {
                    if (forumJson.containsKey(NON_GCONFORUM)) {
                        nonJsonArray = forumJson.getJSONArray(NON_GCONFORUM);
                    }
                    if (forumJson.containsKey(GCONFORUM)) {
                        gcoJsonArray = forumJson.getJSONArray(GCONFORUM);
                    }
                }
            }
            if (body.containsKey(HAS_MORE) && body.getStr(HAS_MORE).equals("1")) {
                pageNoValue += 1;
                getFavorite(bduss, pageNoValue);
            }
            processorData(nonJsonArray, finalData);
            processorData(gcoJsonArray, finalData);
            log.info("获取关注的贴吧结束");
            return finalData;
        } catch (Exception e) {
            log.error("获取关注的贴吧出错" + e);
        }
        return null;
    }

    private static void processorData(JSONArray jsonArray, List<JSONObject> finalData) {
        for (Object o : jsonArray) {
            if (o instanceof JSONArray) {
                processorData((JSONArray) o, finalData);
            } else {
                finalData.add((JSONObject) o);
            }
        }
    }

    /**
     * 获取tbs
     *
     * @return tbs
     */
    private static String getTbs(String bduss) {
        log.info("开始获取tbs");
        Map<String, String> stringStringMap = generateBaseHeaders();
        stringStringMap.put("COOKIE", String.join(EMPTY_STR, BDUSS, EQUAL, bduss));
        try {
            String body = HttpRequest.get(TBS_URL).headerMap(stringStringMap, true).execute().body();
            return new JSONObject(body).getStr(TBS);
        } catch (Exception e) {
            log.info("获取tbs出错");
            return null;
        }
    }

    /**
     * 生成加密sign并放入原数据
     *
     * @param oldData 原数据
     */
    private static void encodeData(JSONObject oldData) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<>(oldData.size());
        for (Map.Entry<String, Object> stringObjectEntry : oldData.entrySet()) {
            keys.add(stringObjectEntry.getKey());
        }
        keys = keys.stream().sorted().collect(Collectors.toList());
        for (String string : keys) {
            sb.append(string).append(EQUAL).append(oldData.getStr(string));
        }
        String sign = DigestUtil.md5Hex(sb.append(SIGN_KEY).toString(), UTF8).toUpperCase();
        oldData.set(SIGN, sign);
    }

    /**
     * 生成header
     *11
     * @return header
     */
    private static Map<String, String> generateBaseHeaders() {
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Host", "tieba.baidu.com");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
        return headers;
    }

    private static JSONObject generateSignData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("_client_type", "2");
        jsonObject.set("_client_version", "9.7.8.0");
        jsonObject.set("_phone_imei", "000000000000000");
        jsonObject.set("model", "MI+5");
        jsonObject.set("net_type", "1");
        return jsonObject;
    }

	//企业应用推送，文档https://developer.work.weixin.qq.com/document/path/90236
    private static void pushMsg(String msg,String 企业ID ,String 企业应用secret,String 企业应用的id) throws IOException {

	// 获取 access_token
    String 请求返回结果 = HttpUtil.get("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+企业ID+"&corpsecret="+企业应用secret);
    JSONObject body = new JSONObject(请求返回结果);
    String access_token = body.getStr("access_token");
    log.info("企业微信应用推送 access token 为 {}", access_token);

    // 构造请求体
    String json = "{\"touser\": \"@all\", "
            + "\"msgtype\": \"text\", "
            + "\"agentid\": "+企业应用的id+", "
            + "\"text\": { "
            + "\"content\": \"" + msg + "\""
            + "} "
            + "}";

    // 发送请求
    String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + access_token;
    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

    // 设置请求头
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");

    // 发送请求体
    con.setDoOutput(true);
    try (OutputStream os = con.getOutputStream()) {
        byte[] input = json.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
    }

    // 获取响应
    int responseCode = con.getResponseCode();
    log.info("Response Code : {}", responseCode);

    StringBuilder response = new StringBuilder();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
    }
    log.info("Response Body : {}", response.toString());
}
    
    public static void sever酱推送(String 消息,String SendKey) throws Exception {
        String url = "https://sctapi.ftqq.com/"+SendKey+".send";
        String title = "贴吧签到情况";
        String message = 消息;
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
