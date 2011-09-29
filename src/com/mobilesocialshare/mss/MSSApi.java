/**
 * 
 */
package com.mobilesocialshare.mss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author victor.pantoja
 *
 */
public class MSSApi {
	
	private static final String TAG = "mss-api";
	
	//public static final String server_name = "http://myalbumshare.com:8000/api";
	private String server_name = "http://192.168.0.255:9080";
	private static final String url_send_context = "/context";	
	private static final String url_login = "/login";
	private static final String url_create_acount = "/login/create";
	private static final String url_create_friendship = "/friendship/create";
	private static final String url_get_friend = "/friendship/get.json";
	private static final String url_remove_friendship = "/friendship/remove";
	private static final String url_send_invite = "/invite/send";
	private static final String url_accept_invite = "/invite/accept";
	private static final String url_get_invitations = "/invitation/get.json";
	private static final String url_send_email_envite = "/invite/email/send";
	private static final String url_accept_email_envite = "/invite/email/accept";
	private static final String url_get_user = "/user.json";
	private static final String url_api_information = "/status";

	public static final String MALE = "M";
	public static final String FEMALE = "F";
	public static final String OTHER = "O";

	public static final Integer TWITTER_ID = 0;
	
	public MSSApi(String server){
		this.server_name = server;
	}

	private String queryRESTurl(String url) {  
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
		HttpConnectionParams.setSoTimeout(params, 10 * 1000);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpConnectionParams.setStaleCheckingEnabled(params, true);
		
		HttpClient httpclient = new DefaultHttpClient(params);

		HttpGet httpget = new HttpGet(this.server_name+url);  
		HttpResponse response;

		try {
			Log.i(TAG, "Querying URL:" + url);
			response = httpclient.execute(httpget);  
			Log.i(TAG, "Status:[" + response.getStatusLine().toString() + "]");  
			HttpEntity entity = response.getEntity();  

			if (entity != null) {  

				InputStream instream = entity.getContent();  
				String result = convertStreamToString(instream);  
				Log.i(TAG, "Result of converstion: [" + result + "]");  

				instream.close();  
				return result;  
			}  
		} catch (ClientProtocolException e) {  
			Log.e(TAG, "There was a protocol based error", e);  
		} catch (IOException e) {  
			Log.e(TAG, "There was an IO Stream related error", e);  
		}
		return "";  
	}
	
	public String tryAuthenticate (String login, String pass)
	{
		String url = url_login+"?username="+login+"&password="+pass;
		
		String result = queryRESTurl(url);
		
		if(result.equals(""))
		{			
			return "";
		}
		
		try{
			JSONObject json = new JSONObject(result);
									
			if (json.getString("status").equals("ok"))
			{	
				return json.getString("msg")+";"+json.getJSONArray("invites").length();
			}
			else{
				return "";
			}
		}  
		catch (JSONException e) {  
			Log.e("JSON", "There was an error parsing the JSON", e);  
		}

		return "";
	}
		
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void Initiate() {
		// TODO Auto-generated method stub
		
	}

	public String Login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public String CreateUser(String string, String string2, String username,
			String male2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void SendContext(Map<Integer, String> context) {
		// TODO Auto-generated method stub
		
	}
}
