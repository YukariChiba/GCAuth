package cc.x7f.gcauth.openid.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cc.x7f.gcauth.openid.GCAuth;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class OAuth {
    public static String auth(String auth_code) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(GCAuth.getConfigStatic().token_endpoint);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id", GCAuth.getConfigStatic().client_id));
        params.add(new BasicNameValuePair("client_secret", GCAuth.getConfigStatic().client_secret));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", auth_code));
        params.add(new BasicNameValuePair("redirect_uri", GCAuth.getConfigStatic().redirect_uri));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            String data = EntityUtils.toString(resEntity);
            return data;
        }
        return null;
    }
}
