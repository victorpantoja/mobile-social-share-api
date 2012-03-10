/**
 * 
 */
package com.mobilesocialshare.mss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
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
	private String server_name = "http://192.168.0.191:9080";
	private static final String url_api_information = "/status";
	private static final String url_login = "/login";
	private static final String url_create_acount = "/login/create";
	private static final String url_send_context = "/context";	
	private static final String url_create_friendship = "/friendship/create";
	private static final String url_get_friend = "/friendship/get.json";
	private static final String url_remove_friendship = "/friendship/remove";
	private static final String url_send_invite = "/invite/send";
	private static final String url_accept_invite = "/invite/accept";
	private static final String url_get_invitations = "/invitation/get.json";
	private static final String url_send_email_envite = "/invite/email/send";
	private static final String url_accept_email_envite = "/invite/email/accept";
	private static final String url_get_user = "/user.json";

	public static final String MALE = "M";
	public static final String FEMALE = "F";
	public static final String OTHER = "O";

	public static final Integer TWITTER_ID = 1;
	
	public MSSApi(String server){
		Log.d(TAG,"Servidor iniciado em: "+server);
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
		String url = url_api_information;
		
		String result = queryRESTurl(url);
	}

	public String Login(String username, String password) {
		String url = url_login+"?username="+username+"&password="+password;
		
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

	public String CreateUser(String email, String lastName, String firstName, String username, String gender) {
		String url = url_create_acount+"?username="+username+"&firstName="+firstName+"&lastName="+lastName+"&email="+email+"&gender="+gender;
		
		return queryRESTurl(url);
	}

	public String sendContext(Map<String, String> context, List<String> apps, String auth) {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(this.server_name+url_send_context+"?auth="+auth);
	    	    
	    List<String> apps_str = new ArrayList<String>();
	    
	    for(String app : apps){
	    	apps_str.add("\""+app+"\"");
	    }
	    	    
	    try {
		    StringEntity se = new StringEntity("{\"application\":"+apps_str.toString()+",\"context\":{\"location\":\""+context.get("location")+"\",\"status\":\""+context.get("status")+"\"}}",HTTP.UTF_8);
	    	
		    httppost.setHeader("Content-Type","application/json;charset=UTF-8");
	    	httppost.setEntity(se);

	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        
			if (entity != null) {  
				InputStream instream = entity.getContent();  
				String result = convertStreamToString(instream);  
				instream.close();  
				return result;  
			}  
	        
	    } catch (ClientProtocolException e) {
	    	 Log.e(TAG,e.getMessage());
	    } catch (IOException e) {
	        Log.e(TAG,e.getMessage());
	    }
	    return "";
	}
	
	public String GetUserInformation(String username, String auth){
		String url = url_get_user+"?username="+username+"&auth="+auth;
		
		return queryRESTurl(url);
	}
	
	public String AcceptInvite(String username, String auth){
		String url = url_accept_invite+"?username="+username+"&auth="+auth;
		
		return queryRESTurl(url);
	}
	
	public String SendInvite(String username, String auth){
		String url = url_send_invite+"?username="+username+"&auth="+auth;
		
		return queryRESTurl(url);
	}
	
	public String RemoveFriendShip(String username, String auth){
		String url = url_remove_friendship+"?username="+username+"&auth="+auth;
		
		return queryRESTurl(url);
	}
	
	public String GetFriends(String auth){
		String url = url_get_friend+"?auth="+auth;
		return queryRESTurl(url);
	}
	
	public String GetInvitations(String auth){
		String url = url_get_invitations+"?auth="+auth;
		return queryRESTurl(url);
	}
	
	public String SendEmailInvite(String email, String auth){
		String url = url_send_email_envite+"?email="+email+"&auth="+auth;
		
		return queryRESTurl(url);
	}
	
	public static String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
}
