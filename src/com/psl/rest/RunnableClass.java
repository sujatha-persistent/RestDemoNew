package com.psl.rest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.json.JSONException;
import org.json.JSONObject;

import com.jaxb.Configuration;
import com.jaxb.ObjNameEnum;
import com.jaxb.Organization;
import com.jaxb.SObject;

public class RunnableClass implements Runnable{

	@Override
	public void run() {

		System.out.println("Starting run method");

		Properties prop = new Properties();
		String fileName = FilesPath.pathForOauthProp;
		InputStream input = null;
		try {
			input = new FileInputStream(fileName);
			prop.load(input);
			input.close();

			String accessToken = prop.getProperty("access_token");
			String instanceUrl = prop.getProperty("instanceURL");
			String expire_time = prop.getProperty("expire_time");
			String refresh_token = prop.getProperty("refresh_token");
			String client_secret = prop.getProperty("client_secret");
			String clientId = prop.getProperty("clientId");
			String tokenUrl = prop.getProperty("tokenUrl");
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			Date d1 = format.parse(expire_time);
			Date d2 = new Date();

			long diff = d1.getTime() - d2.getTime();
			long diffMinutes = diff / (60 * 1000) % 60;//360
			if(diff <0 || diffMinutes >= 360){
				RefreshingAccessToken refreshingMethod = new RefreshingAccessToken();
				accessToken = refreshingMethod.execAccessToken(refresh_token, clientId, client_secret, tokenUrl);
			}

			GetObjectResponse authResp = new GetObjectResponse();
			JSONObject accntRsp = new JSONObject();

			File filename = new File(FilesPath.pathForsfdcConfxml);
			JAXBContext jc = JAXBContext.newInstance(Configuration.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Configuration configHome = (Configuration) unmarshaller.unmarshal(filename);
			List<Organization> orgList = configHome.getOrganizations().getOrganization();

			for(Organization org:orgList){
				String orgId = org.getId();

				String path = FilesPath.pathForContentFolder+orgId;
				File file = new File(path);

				String timestamp =  new java.text.SimpleDateFormat("dd-MM-yyyy-HH-mm-SS").format(new Date());
				String pathforTS = path+'/'+timestamp;
				File timeStampFolder = new File(pathforTS);

				if (!file.exists()) {
					file.mkdir();
				}

				if (!timeStampFolder.exists()) {
					timeStampFolder.mkdir();
				}

				for(SObject sObj: org.getSObjects().getSObject()){
					ObjNameEnum name = sObj.getName();

					String pathforSobj = pathforTS+'/'+name;
					File sobjects = new File(pathforSobj);

					if (!sobjects.exists()) {
						sobjects.mkdir();
					}

					String pathAcntFile = pathforSobj+"/"+name+".json";
					accntRsp = authResp.showAccounts(instanceUrl, accessToken,new Date(),name);
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathAcntFile));
					bufferedWriter.write(accntRsp.toString(2));
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			}
		}catch(JAXBException | IOException e){
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
