package com.example.demo;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);
	}

	public List<Object> printlocations() throws IOException {
		URL obj = new URL("https://umassdining.com/uapp/get_infov2");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			JSONTokener tokener = new JSONTokener(in);
			JSONArray json = new JSONArray(tokener);
			return json.toList();
		}
		else {
			return Collections.emptyList();
		}
	}
	@GetMapping
	public String getmenu(@RequestParam String food) throws IOException {
		URL obj = new URL("https://umassdining.com/foodpro-menu-ajax?tid=0&date=11%2F12%2F2022");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			JSONTokener tokener = new JSONTokener(in);
			Map<String, Object> json = new JSONObject(tokener).toMap();
			StringBuilder keys = new StringBuilder();
			for(String meal : json.keySet()) {
				keys.append(json.get(meal).toString());
			}
			return keys.toString();
		}
		return "Your amazing bot is sorry to report that " + food + "is not offered in any of the 4 main dining commons " +
				"on campus :((((. However, in my greatness I have found an abundance of delicious food across the main " +
				"dining commons. I implore you to search for more of your favorite foods, I am certain they are offered " +
				"somewhere today!";
	}
}

