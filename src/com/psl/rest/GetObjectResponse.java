package com.psl.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GetObjectResponse {
	public JSONObject showAccounts(String  instanceUrl, String accessToken,
			Date date) throws  IOException,FileNotFoundException {
		CloseableHttpClient  httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet();

		//add key and value
		httpGet.addHeader("Authorization", "OAuth " + accessToken);
		JSONObject authResponse = new JSONObject();

		String fileName = FilesPath.pathForLastSyncDates;
		FileInputStream in = new FileInputStream(fileName);
		Properties prop = new Properties();
		prop.load(in);
		in.close();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'z'");
		try {
			URIBuilder builder = new URIBuilder(instanceUrl+ "/services/data/v30.0/query");
			String lastSyncDate = prop.getProperty("lastSyncDate");

			if(lastSyncDate != null && !lastSyncDate.equals("")){
				String query = "SELECT Name, Id from Account where LastModifiedDate >"+ lastSyncDate;
				builder.setParameter("q", query);
			}else{
				builder.setParameter("q", "SELECT Name, Id from Account LIMIT 100");
			}
			if(date != null){
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				prop.setProperty("lastSyncDate", df.format(date));
				FileOutputStream out = new FileOutputStream(fileName);
				prop.store(out, null);
				out.close();
			}

			httpGet.setURI(builder.build());

			CloseableHttpResponse  closeableresponse = httpclient.execute(httpGet);
			System.out.println("Response Status line :" + closeableresponse.getStatusLine());

			if (closeableresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try {
					HttpEntity entity = closeableresponse.getEntity();
					InputStream rstream = entity.getContent();
					authResponse = new JSONObject(new JSONTokener(rstream));
					System.out.println("Query response: " + authResponse.toString(2));
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (URISyntaxException  e1) {
			// TODO Auto­generated catch block
			e1.printStackTrace();
		} finally {
			httpclient.close();
		}
		return authResponse;
	}
}
