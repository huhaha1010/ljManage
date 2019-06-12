package com.mr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 自定义响应结构
 */
public class JsonUtils {
    /**
     * 将json文件转换为字符串
     * @author hhl
     */
    public static String jsonFile2String(String fileName) {
    	String jsonStr = "";
    	try {
			File jsonFile = new File(fileName);
			FileReader fileReader = new FileReader(jsonFile);
			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char)ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
}
