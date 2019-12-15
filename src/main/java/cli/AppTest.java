package cli;

import org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import junit.framework.Assert;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import org.json.JSONObject;

public class AppTest {

  HttpClient client;

  @Before
  public void init( ){
    client = new DefaultHttpClient();
  }

  @Test
  public void testLogin() 
    throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8080/rest/energy/login");
 
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("location", "Earth"));
    params.add(new BasicNameValuePair("password", "forklift154"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));
 
    HttpResponse response = client.execute(httpPost);

    String responseString = new BasicResponseHandler().handleResponse(response);
    System.out.println(responseString);
    JSONObject obj = new JSONObject(responseString);
    int code = obj.getInt("statusCode");

    System.out.println("HTTP resp " + response.getStatusLine().getStatusCode());

    Assert.assertEquals(200, code);

  }
}

