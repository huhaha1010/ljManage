package com.mr.util;

import com.alibaba.fastjson.JSONObject;
import com.mr.config.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PhoneUtil {
    /**
     * 手机发送短信验证码
     *
     * @author hhl
     */
    public static boolean sendPhoneCode(String phoneNumber, String rod) {
        long timestamp = getTimestamp();
        String sig = getMD5(Config.ACCOUNT_SID, Config.AUTH_TOKEN, timestamp);
        String tamp = "【北京灵境科技有限公司】您的验证码为：" + rod + "，该验证码5分钟内有效。请勿泄漏于他人。";
        OutputStreamWriter out = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(Config.PHONE_VERIFICATION_CODE_API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);// 设置是否允许数据写入
            connection.setDoOutput(true);// 设置是否允许参数数据输出
            connection.setConnectTimeout(5000);// 设置链接响应时间
            connection.setReadTimeout(10000);// 设置参数读取时间
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            // 提交请求
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            String args = getQueryArgs(Config.ACCOUNT_SID, tamp, phoneNumber, timestamp, sig, "JSON");
            out.write(args);
            out.flush();
            // 读取返回参数

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                result.append(temp);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(result.toString());
        JSONObject json = JSONObject.parseObject(result.toString());
        String respCode = json.getString("respCode");
        String defaultRespCode = "0000";
        if (defaultRespCode.equals(respCode) && json.getJSONArray("failList").size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // 定义一个请求参数拼接方法
    public static String getQueryArgs(String accountSid, String smsContent, String to, long timestamp, String sig,
                                      String respDataType) {
        return "accountSid=" + accountSid + "&smsContent=" + smsContent + "&to=" + to + "&"+"timestamp=" + timestamp
                + "&sig=" + sig + "&respDataType=" + respDataType;
    }

    // 获取时间戳
    public static Long getTimestamp() {
//		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date().getTime());
        return System.currentTimeMillis();
    }

    // sing签名
    public static String getMD5(String sid, String token, Long timestamp) {

        StringBuilder result = new StringBuilder();
        String source = sid + token + timestamp;
        // 获取某个类的实例
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 要进行加密的东西
            byte[] bytes = digest.digest(source.getBytes());
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) {
                    result.append("0" + hex);
                } else {
                    result.append(hex);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result.toString();
    }
}
