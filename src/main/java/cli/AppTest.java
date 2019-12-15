package cli;

import org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
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
import org.json.JSONArray;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

  public static HttpClient client;
  public static String token;

  @Before
  public void init( ){
    client = new DefaultHttpClient();
  }

  @Test
  public void aTestLogin() // first test
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
    token = obj.getString("message");
    System.out.println(token);

    System.out.println("HTTP resp " + response.getStatusLine().getStatusCode());

    Assert.assertEquals(200, code);
  }

  @Test
  public void bTestInvalidEnergyRequest() 
    throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8080/rest/energy/request");
 
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("token", "0"));
    params.add(new BasicNameValuePair("requiredEnergy", "100"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));
 
    HttpResponse response = client.execute(httpPost);

    String responseString = new BasicResponseHandler().handleResponse(response);
    System.out.println(responseString);
    JSONObject obj = new JSONObject(responseString);
    int code = obj.getInt("statusCode");

    System.out.println("HTTP resp " + response.getStatusLine().getStatusCode());

    Assert.assertEquals(500, code);
  }

  @Test
  public void cTestValidEnergyRequest() 
    throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8080/rest/energy/request");
 
    System.out.println(token);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("token", token));
    params.add(new BasicNameValuePair("requiredEnergy", "100"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));
 
    HttpResponse response = client.execute(httpPost);

    String responseString = new BasicResponseHandler().handleResponse(response);
    System.out.println(responseString);
    JSONObject obj = new JSONObject(responseString);
    int code = obj.getInt("statusCode");

    System.out.println("HTTP resp " + response.getStatusLine().getStatusCode());

    Assert.assertEquals(200, code);
  }


  @Test
  public void testTooMuchEnergyRequest() 
    throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8080/rest/energy/request");
 
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("token", token));
    params.add(new BasicNameValuePair("requiredEnergy", "100000000000000"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    HttpResponse response = client.execute(httpPost);

    int fail = 0;
    try {
      String responseString = new BasicResponseHandler().handleResponse(response);
    } catch (Exception e) {
      fail = 1;
    }

    Assert.assertEquals(1, fail);
  }

  @Test
  public void testNegativeEnergyRequest() 
    throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8080/rest/energy/request");
 
    System.out.println(token);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("token", token));
    params.add(new BasicNameValuePair("requiredEnergy", "-1"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));
 
    HttpResponse response = client.execute(httpPost);

    String responseString = new BasicResponseHandler().handleResponse(response);
    System.out.println(responseString);
    JSONObject obj = new JSONObject(responseString);
    int code = obj.getInt("statusCode");

    System.out.println("HTTP resp " + response.getStatusLine().getStatusCode());

    Assert.assertEquals(500, code);
  }

  @Test
  public void testCheckRequestCorrect() 
    throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8080/rest/energy/check_requests");
 
    System.out.println(token);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("token", token));
    httpPost.setEntity(new UrlEncodedFormEntity(params));
 
    HttpResponse response = client.execute(httpPost);

    String responseString = new BasicResponseHandler().handleResponse(response);
    System.out.println(responseString);
    System.out.println("HTTP resp " + response.getStatusLine().getStatusCode());

    Assert.assertTrue(responseString != null);
  }

  @Test
  public void testCheckRequestIncorrect() 
    throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost("http://localhost:8080/rest/energy/check_requests");
 
    System.out.println(token);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("token", "0"));
    httpPost.setEntity(new UrlEncodedFormEntity(params));
 
    HttpResponse response = client.execute(httpPost);

    String responseString = new BasicResponseHandler().handleResponse(response);
    System.out.println(responseString);
    JSONObject obj = new JSONObject(responseString);
    int code = obj.getInt("statusCode");

    System.out.println("HTTP resp " + response.getStatusLine().getStatusCode());

    Assert.assertEquals(500, code);
  }
}

