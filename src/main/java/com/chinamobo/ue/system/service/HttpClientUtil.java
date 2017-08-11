package com.chinamobo.ue.system.service;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientUtil {
	 private static final CloseableHttpClient hc;

	    static {
	        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

	        cm.setDefaultMaxPerRoute(20);
	        cm.setMaxTotal(200);

	        hc = HttpClients.custom().setConnectionManager(cm).build();
	    }
	    public static int postForm(String url, Map<String, String> params, HttpEntity entity) throws IOException {
	        HttpPost post = new HttpPost(url);
	        for(Map.Entry<String, String> param : params.entrySet()) {
	            post.setHeader(param.getKey(), param.getValue());
	        }
	        post.setEntity(entity);
	        CloseableHttpResponse resp = null;
	        try {
	            resp = hc.execute(post);
	            return resp.getStatusLine().getStatusCode();
	        } finally {
	            if(resp != null) {
	                try {
	                    resp.close();
	                } catch (IOException e1) {
	                	
	                }
	            }
	        }
	    }
}
