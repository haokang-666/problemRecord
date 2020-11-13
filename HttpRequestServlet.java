package com.aisino.bec.platform.integrated.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequestServlet {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestServlet.class);

    private static final String APPLICATION_JSON = "application/json";
    
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
	 * @throws IOException 
     */
    @ResponseStatus
    public static String sendPost(String url, String param) throws   Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);  
            URLConnection conn = realUrl.openConnection(); 
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", APPLICATION_JSON);
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)"); 
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true); 
           
            out = new PrintWriter(conn.getOutputStream()); 
            out.print(param); 
            out.flush();  
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	logger.error("访问的url:" + url+";"+"访问的参数param:" + param+";"+"返回值result:"+result,ex);
            }
        }
        return result;
    }
    
	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, int timeOut) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url); 
            URLConnection conn = realUrl.openConnection(); 
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)"); 
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            conn.setDoOutput(true);
            conn.setDoInput(true); 
            out = new PrintWriter(conn.getOutputStream()); 
            out.print(param); 
            out.flush();  
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                logger.error("sendPost异常",ex);
            }
        }
        return result;
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) throws Exception {
//    	logger.error("访问的url:" + url+";"+"访问的参数param:" + param+";");
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString); 
            URLConnection connection = realUrl.openConnection(); 
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)"); 
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect(); 
            Map<String, List<String>> map = connection.getHeaderFields();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("sendGet异常",e2);
            }
        }
//        logger.error("访问的url:" + url+";"+"访问的参数param:" + param+";"+"返回值result:"+result);
        return result;
    }

}
