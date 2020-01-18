package com.zteng.moraleducation.config;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * spring的RestTemplate连接池相关配置
 *
 * @project common-utils
 * @fileName RestTemplateConfiguration.java
 * @Description
 * @author light-zhang
 * @date 2019年4月29日
 * @version 1.0.0
 */
@Configuration
public class RestTemplatePoolConfig {
    /**
     * 让spring管理RestTemplate,参数相关配置
     *
     * @param builder
     * @return
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();// 生成一个RestTemplate实例
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }

    /**
     * 客户端请求链接策略
     *
     * @return
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClientBuilder().build());
        clientHttpRequestFactory.setConnectTimeout(6000); // 客服端发送请求到与目标url建立起连接的最大时间
        clientHttpRequestFactory.setReadTimeout(30000); // 目标url建立连接后，等待放回response的最大时间
        clientHttpRequestFactory.setConnectionRequestTimeout(10000);// 从连接池中获取可用连接超时
        return clientHttpRequestFactory;
    }

    /**
     * 设置HTTP连接管理器,连接池相关配置管理
     *
     * @return 客户端链接管理器
     */
    @Bean
    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //加入重试
//        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, false));
        httpClientBuilder.setConnectionManager(poolingConnectionManager());
        return httpClientBuilder;
    }

    /**
     * 链接线程池管理,可以keep-alive不断开链接请求,这样速度会更快
     * MaxTotal 连接池最大连接数
     * DefaultMaxPerRoute 每个主机的并发
     * ValidateAfterInactivity 可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
     * @return
     */
    @Bean
    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(1000);
        poolingConnectionManager.setDefaultMaxPerRoute(100);
        poolingConnectionManager.setValidateAfterInactivity(30000);
        return poolingConnectionManager;
    }
}
