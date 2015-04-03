package com.psl.rest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Util {
	public void writingOauthProps(String accesstoken, String instanceUrl, String refresh_token, String tokenUrl, String clientId, String clientSecret) throws IOException{

		String fileName = FilesPath.pathForOauthProp;
		Properties prop = new Properties();

		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

		prop.setProperty("access_token", accesstoken);
		prop.setProperty("instanceURL",instanceUrl);
		prop.setProperty("refresh_token", refresh_token);
		prop.setProperty("start_time", ""+format.format(cal.getTime()));
		cal.add(Calendar.HOUR_OF_DAY, 6);
		prop.setProperty("expire_time", ""+format.format(cal.getTime()));
		prop.setProperty("tokenUrl", tokenUrl);
		prop.setProperty("clientId", ""+clientId);
		prop.setProperty("client_secret",""+clientSecret);

		FileOutputStream out = new FileOutputStream(fileName);
		prop.store(out, null);
		out.close();
	}
}
