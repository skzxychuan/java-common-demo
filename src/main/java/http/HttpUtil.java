package http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 1. 先在pom文件导入一个http工具，有很多种，这里用的是apache的httpclient
 * 2  看下面例子
 */
@Log4j2
public class HttpUtil {
    private static final String CHARSET = "UTF-8";

    public static void doGet() throws IOException {
        String url = "http://192.168.1.100:9200/_cat/health?v";

        //创建CloseableHttpClient
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = httpClientBuilder.build();

        //创建Get请求
        HttpUriRequest httpGet = new HttpGet(url);

        //执行, 获取HttpResponse对象
        CloseableHttpResponse response = client.execute(httpGet);
        //打印出来可以看到其实就是响应协议包的封装
        System.out.println(response.toString());

        //获取响应体
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            System.out.println(EntityUtils.toString(entity, "utf-8"));
        }
    }

    public static JSONObject doGet(String url, Header... headers) throws Exception {
        log.info("[GET] " + url);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = clientBuilder.build();

        HttpGet httpGet = new HttpGet(url);
        if (null != headers) {
            for (Header header : headers) {
                if (null != header) {
                    httpGet.setHeader(header);
                }
            }
        }

        CloseableHttpResponse response = client.execute(httpGet);
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            response.close();
            client.close();
            throw new Exception("GET请求失败, status：" + response.getStatusLine().getStatusCode() + " url: " + url);
        }
        HttpEntity resEntity = response.getEntity();
        if (null == resEntity) {
            response.close();
            client.close();
            throw new Exception("GET请求失败,响应体为null " + url);
        }

        JSONObject resObj = JSON.parseObject(EntityUtils.toString(resEntity, CHARSET));
        response.close();
        client.close();

//        log.info("[请求响应] " + resObj.toJSONString());

        return resObj;
    }

    public static void doPost() throws IOException {
        String url = "http://www.xinghengedu.com/autotele/simpleLogin.htm";

        //创建CloseableHttpClient
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = httpClientBuilder.build();

        //创建Post请求
        HttpUriRequest httpPost = new HttpPost(url);

        //设置请求体参数
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("username", "13121939122");
        BasicNameValuePair param2 = new BasicNameValuePair("password", "123456");
        list.add(param1);
        list.add(param2);

        //请求体参数序列化
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "utf-8");

        //装填参数并执行
        ((HttpPost) httpPost).setEntity(entityParam);
        CloseableHttpResponse response = client.execute(httpPost);
        //响应包(的封装)
        System.out.println(response.toString());

        //响应体
        HttpEntity entity = response.getEntity();
        if(entity!=null){
            String  entityStr= EntityUtils.toString(entity,"utf-8");
            System.out.println(entityStr);
        }
    }

    public static JSONObject doPost(String url, Header header, HttpEntity entity) throws Exception {
        log.info("[POST] " + url);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = clientBuilder.build();

        HttpPost httpPost =  new HttpPost(url);
        httpPost.setHeader(header);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            response.close();
            client.close();
            throw new Exception("POST请求失败, status：" + response.getStatusLine().getStatusCode() + " url: " + url);
        }
        HttpEntity resEntity = response.getEntity();
        if (null == resEntity) {
            response.close();
            client.close();
            throw new Exception("POST请求失败,响应体为null" + url);
        }

        JSONObject resObj = JSON.parseObject(EntityUtils.toString(resEntity, CHARSET));
        response.close();
        client.close();

        return resObj;
    }

    public static JSONObject doPostJson(String url, String json) throws Exception {
//        log.info(Thread.currentThread().toString() + "\t[请求JSON] " + json);
        log.info("[POST] " + url);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = clientBuilder.build();

        HttpUriRequest httpPost =  new HttpPost(url);
        httpPost.setHeader("content-type", "application/json; charset=UTF-8");
        //下面这样会导致JSON中的中文乱码
//        StringEntity reqEntity = new StringEntity(json);
//        reqEntity.setContentType("application/json");
//        reqEntity.setContentEncoding(CHARSET);
        StringEntity reqEntity = new StringEntity(json, CHARSET);
        reqEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        ((HttpPost) httpPost).setEntity(reqEntity);

        CloseableHttpResponse response = client.execute(httpPost);
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            response.close();
            client.close();
            throw new Exception("POST请求失败, status：" + response.getStatusLine().getStatusCode() + " url: " + url);
        }
        HttpEntity resEntity = response.getEntity();
        if (null == resEntity) {
            response.close();
            client.close();
            throw new Exception("POST请求失败,响应体为null" + url);
        }

        JSONObject resObj = JSON.parseObject(EntityUtils.toString(resEntity, CHARSET));
        response.close();
        client.close();

//        log.info("[请求响应] " + resObj.toJSONString());

        return resObj;
    }

    public static JSONObject doPut(String url, String json) throws Exception {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = clientBuilder.build();

        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("content-type", "application/json; charset=UTF-8");

        StringEntity entity = new StringEntity(json, CHARSET);
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPut.setEntity(entity);

        log.info(Thread.currentThread().toString() + "\t[请求JSON] " + json);
        CloseableHttpResponse response = client.execute(httpPut);
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            response.close();
            client.close();
            throw new Exception("PUT请求失败, status：" + response.getStatusLine().getStatusCode() + " url: " + url);
        }
        HttpEntity resEntity = response.getEntity();
        if (null == resEntity) {
            response.close();
            client.close();
            throw new Exception("PUT请求失败,响应体为null" + url);
        }

        JSONObject resObj = JSON.parseObject(EntityUtils.toString(resEntity, CHARSET));
        response.close();
        client.close();

        log.info("[请求响应] " + resObj.toJSONString());

        return resObj;
    }

    public static int doHead(String url) throws Exception {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = clientBuilder.build();
        HttpHead httpHead = new HttpHead(url);
        CloseableHttpResponse response = client.execute(httpHead);
        return response.getStatusLine().getStatusCode();
    }
}
