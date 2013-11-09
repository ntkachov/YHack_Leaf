package com.dutchrudder.leaf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.util.Log;

public class ServerManager {

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
			throw new RuntimeException(e);
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
			throw new RuntimeException(e);
		}

		return null;
	}

}
