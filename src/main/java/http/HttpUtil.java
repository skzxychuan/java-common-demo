package http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 1. 先在pom文件导入一个http工具，有很多种，这里用的是apache的httpclient
 * 2  看下面例子
 */
public class HttpUtil {

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
}
