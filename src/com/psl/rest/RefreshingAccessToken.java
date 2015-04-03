package com.psl.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RefreshingAccessToken{
	public String execAccessToken(String refresh_token, String clientId,
			String clientSecret, String tokenUrl) throws ClientProtocolException, IOException {
		String accessToken = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpost = new HttpPost(tokenUrl);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("grant_type", "refresh_token"));
		nvps.add(new BasicNameValuePair("client_id", clientId));
		nvps.add(new BasicNameValuePair("client_secret", clientSecret));
		nvps.add(new BasicNameValuePair("refresh_token",refresh_token));
		httpost.setEntity(new UrlEncodedFormEntity(nvps));

		// Execute the request.
		CloseableHttpResponse closeableresponse=httpClient.execute(httpost);
		System.out.println("\n\nResponse Statusline in refresh token:"+closeableresponse.getStatusLine());

		try {
			// Do the needful with entity.
			HttpEntity entity = closeableresponse.getEntity();
			InputStream rstream = entity.getContent();
			JSONObject authResponse = new JSONObject(new JSONTokener(rstream));

			System.out.println("authResponse for refresh token::"+authResponse);

			accessToken = authResponse.getString("accessToken");
			Util util = new Util();
			util.writingOauthProps(accessToken,authResponse.getString("instanceUrl"),refresh_token,tokenUrl,clientId,clientSecret);
		} catch (JSONException e) {
			// TODO Auto­generated catch block e.printStackTrace();
			e.printStackTrace();
		} finally {
			// Closing the response
			closeableresponse.close();
		}

		return accessToken;
	}
}
