package com.dutchrudder.leaf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ServerManager {
	
	private static String server = "http://54.215.16.181/";

	public static boolean sendFirst() {

		try {
			URL u = new URL(
					String.format("http://54.215.16.181/send-time?&timeStamp=0&userId=mchiang&userInfo=Marco_Chiang"));

			HttpURLConnection urlConnection = (HttpURLConnection) u
					.openConnection();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream(),
								"UTF-8"));
				String json = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					json += line;
				}

				JSONObject jsonObject = new JSONObject(json);
				Log.d("Leaf", jsonObject.toString());
				if (jsonObject.get("message").equals("success")) {
					return true;
				}

			} finally {
				urlConnection.disconnect();
			}

		} catch (Exception e) {
			//eat it
		}

		return false;
	}

	public static String sendSecond() {

		try {
			URL u = new URL(
					String.format("http://54.215.16.181/match?&timeStamp=0&userId=test"));

			HttpURLConnection urlConnection = (HttpURLConnection) u
					.openConnection();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream(),
								"UTF-8"));
				String json = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					json += line;
				}

				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.get("message").equals("success")) {
					return jsonObject.getString("userInfo");
				}

			} finally {
				urlConnection.disconnect();
			}

		} catch (Exception e) {
			//eat it
		}

		return null;
	}
	
	public static boolean sendUserReg(String user, String first, String full, String facebook){
		try {
			
			String info = first +"&#169;" + full + "&#169;" + facebook;
			String surl = server + "sign-up?username=" + user + "&password=u&userInfo=" + info;
			URL url = new URL(String.format(surl));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream(),
								"UTF-8"));
				String json = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					json += line;
				}

				JSONObject jsonObject = new JSONObject(json);
				Log.d("Leaf", jsonObject.toString());
				if (jsonObject.get("message").equals("success")) {
					success = true;
					return true;
				}
			}catch(Exception e){
				Log.e("Leaf", e.getLocalizedMessage(), e);
			} finally {
				
				connection.disconnect();
			}

		} catch (Exception e) {
			//eat it
		}
		success = false;
		return false;
	}
	
	public static boolean success;
	
	public static void ServerTask(Runnable background, Runnable callback, Runnable callbackErr){
		Runnable[] ra = {background, callback, callbackErr};
		ServerTask task = new ServerTask();
		task.execute(ra);
	}
	
	private static class ServerTask extends AsyncTask<Runnable, Void, Boolean>{
		
		Runnable background, callback, callbackErr;

		@Override
		protected Boolean doInBackground(Runnable... params) {
			background = params[0];
			callback = params[1];
			callbackErr = params[2];
			background.run();
			return success;
		}
		
		 protected void onProgressUpdate(Integer... progress) {
	     }

	     protected void onPostExecute(Boolean result) {
	    	 if(result){
	    		 callback.run();
	    	 } else {
	    		 callbackErr.run();
	    	 }
	     }
		
	}

}
