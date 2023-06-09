import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HttpPostExample {

    public static void main(String[] args) throws Exception {
		String a="RQZyWavFvn_QP7s5F9C1ptrEObxlklJXaNAW1FDcuSUTjg2ZwG0bKdGcizeIyfJdUiG8L5ntECoEXb-TMqY6K9C5ukuueqv5eYvgePDFiEqU47cffEIEq_arxz9B6XjA-Exj8VYhV2AlahL3JUoMMZwcgT-10GPW2VMuU2Kgy_UQNzmJAPOEcKsdY8sA5abQV-fpyv7D9_ghrwn8ROEj7A";
		
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+a;
        String json = "{\r\n"
        		+ "    \"touser\": \"@all\",\r\n"
        		+ "    \"toparty\": \"PartyID1|PartyID2\",\r\n"
        		+ "    \"totag\": \"TagID1 | TagID2\",\r\n"
        		+ "    \"msgtype\": \"text\",\r\n"
        		+ "    \"agentid\": 1000002,\r\n"
        		+ "    \"text\": {\r\n"
        		+ "        \"content\": \"你的快递已到，请携带工卡前往邮件中心领取。\\n出发前可查看<a href=\\\"http://work.weixin.qq.com\\\">邮件中心视频实况</a>，聪明避开排队。\"\r\n"
        		+ "    },\r\n"
        		+ "    \"safe\": 0,\r\n"
        		+ "    \"enable_id_trans\": 0,\r\n"
        		+ "    \"enable_duplicate_check\": 0\r\n"
        		+ "}";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // 设置POST请求的方法和请求头
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        // 向服务器发送数据
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 获取服务器响应
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }
        System.out.println("Response Body : " + response.toString());
    }
}