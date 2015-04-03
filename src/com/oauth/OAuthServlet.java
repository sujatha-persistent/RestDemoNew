package com.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.psl.rest.Util;

/**
 * Servlet parameters
 */
@WebServlet(name = "oauth", urlPatterns = { "/oauth/*", "/oauth" }, initParams = {
		// clientId is 'Consumer Key' in the Remote Access UI
		@WebInitParam(name = "clientId", value = "3MVG9ZL0ppGP5UrCQde9sWEtnQDqOTFGPnJmkD9APxVHGaNURH4NHD9miTrWhIiZs0EYNsRkR2pz46asTPs6s"),
		// clientSecret is 'Consumer Secret' in the Remote Access UI
		@WebInitParam(name = "clientSecret", value = "4996200176306771234"),
		// This must be identical to 'Callback URL' in the Remote Access UI
		@WebInitParam(name = "redirectUri", value = "http://localhost:8080/RestApp/oauth/_callback"),
		@WebInitParam(name = "environment", value = "https://login.salesforce.com"), })
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	private static final String INSTANCE_URL = "INSTANCE_URL";

	public String clientId = null;
	public String clientSecret = null;
	public String redirectUri = null;
	public String environment = null;
	public String authUrl = null;
	public String tokenUrl = null;

	public void init() throws ServletException {
		clientId = this.getInitParameter("clientId");
		clientSecret = this.getInitParameter("clientSecret");
		redirectUri = this.getInitParameter("redirectUri");
		environment = this.getInitParameter("environment");

		try {
			authUrl = environment
					+ "/services/oauth2/authorize?response_type=code&client_id="+ clientId+"&redirect_uri="
					+ URLEncoder.encode(redirectUri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServletException(e);
		}

		tokenUrl = environment + "/services/oauth2/token";
	}


	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);

		if (accessToken == null) {
			String instanceUrl = null;
			String refresh_token = null;
			if (request.getRequestURI().endsWith("oauth")) {
				// we need to send the user to authorize
				response.sendRedirect(authUrl);
				return;
			} else {
				System.out.println("Auth succussful - got call back");
				String code = request.getParameter("code"); 

				CloseableHttpClient httpClient = HttpClients.createDefault();
				try{
					HttpPost httpost = new HttpPost(tokenUrl);

					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("code", code));
					nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
					nvps.add(new BasicNameValuePair("client_id", clientId));
					nvps.add(new BasicNameValuePair("client_secret", clientSecret));
					nvps.add(new BasicNameValuePair("redirect_uri", redirectUri));
					httpost.setEntity(new UrlEncodedFormEntity(nvps));

					// Execute the request.
					CloseableHttpResponse closeableresponse=httpClient.execute(httpost);
					System.out.println("Response Statusline:"+closeableresponse.getStatusLine());

					try {
						// Do the needful with entity.
						HttpEntity entity = closeableresponse.getEntity();
						InputStream rstream = entity.getContent();
						JSONObject authResponse = new JSONObject(new JSONTokener(rstream));

						System.out.println("authResponse for refresh token::"+authResponse);

						accessToken = authResponse.getString("access_token");
						instanceUrl = authResponse.getString("instance_url");
						refresh_token = authResponse.getString("refresh_token");

						Util util = new Util();
						util.writingOauthProps(accessToken,instanceUrl,refresh_token,tokenUrl,clientId,clientSecret);
					} catch (JSONException e) {
						// TODO Auto­generated catch block e.printStackTrace();
						e.printStackTrace();
					} finally {
						// Closing the response
						closeableresponse.close();
					}
				}finally{
					httpClient.close();
				}
			}

			// Set a session attribute so that other servlets can get the access
			// token
			request.getSession().setAttribute(ACCESS_TOKEN, accessToken);

			// We also get the instance URL from the OAuth response, so set it
			// in the session too
			request.getSession().setAttribute(INSTANCE_URL, instanceUrl);
			request.getSession().setAttribute(REFRESH_TOKEN, refresh_token);

		}
		response.sendRedirect(request.getContextPath() + "/DemoRest");
	}
}
