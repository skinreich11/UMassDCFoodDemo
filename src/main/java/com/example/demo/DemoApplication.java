package com.example.demo;


import java.util.regex.Pattern;
import java.time.LocalDate;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/testing")
	public ArrayList<String> work() throws IOException {
		LocalDate date = LocalDate.now();
		ArrayList<String> result = new ArrayList<>();
		URL link = new URL("https://umassdining.com/foodpro-menu-ajax?tid=1&date=" + date.getMonthValue() +
				"%2F" + date.getDayOfMonth() + "%2F" + date.getYear());
		HttpURLConnection con = (HttpURLConnection) link.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			JSONTokener tokener = new JSONTokener(in);
			JSONObject json = new JSONObject(tokener);
			Iterator<String> keys = json.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				if (json.get(key) instanceof JSONObject) {
					Iterator<String> keys2 = ((JSONObject) json.get(key)).keys();
					while (keys2.hasNext()) {
						String key2 = keys2.next();
						if (((JSONObject) json.get(key)).get(key2) instanceof String) {
							}
						}
					}
				}
			return result;
			}
		return null;
	}

	@GetMapping("/getfood")
	public StringBuilder getmenu(@RequestParam String food) throws IOException {
		LocalDate date = LocalDate.now();
		ArrayList<String[]> allfoods = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			URL link = new URL("https://umassdining.com/foodpro-menu-ajax?tid=" + i + "&date=" + date.getMonthValue() +
					"%2F" + date.getDayOfMonth() + "%2F" + date.getYear());
			HttpURLConnection con = (HttpURLConnection) link.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				JSONTokener tokener = new JSONTokener(in);
				JSONObject json = new JSONObject(tokener);
				Iterator<String> keys = json.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					if (json.get(key) instanceof JSONObject) {
						Iterator<String> keys2 = ((JSONObject) json.get(key)).keys();
						while (keys2.hasNext()) {
							String key2 = keys2.next();
							if (((JSONObject) json.get(key)).get(key2) instanceof String) {
								Pattern pattern = Pattern.compile("(?<=data-dish-name)(.*?)(?=\" )");
								Matcher matcher = pattern.matcher((String) (((JSONObject) json.get(key)).get(key2)));
								while (matcher.find()) {
									if(matcher.group(1).substring(2).equalsIgnoreCase(food)){
										switch (i) {
											case 1:
												allfoods.add(new String[]{"Worcester", key2, key});
												break;
											case 2:
												allfoods.add(new String[]{"Franklin", key2, key});
												break;
											case 3:
												allfoods.add(new String[]{"Hampshire", key2, key});
												break;
											default:
												allfoods.add(new String[]{"Berkshire", key2, key});
												break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (allfoods.size() != 0) {
			StringBuilder answer = new StringBuilder("Oh Boy, Do I have good new for you! I have found your favorite food in at least one of the " +
					"UMass award winning dining commons. The most delicious " + food + " can be found at ");
			for (int i = 0; i < allfoods.size(); i++) {
				if (i == allfoods.size() - 1 && i == 0) {
					answer.append(allfoods.get(i)[0]).append(" in the ").append(allfoods.get(i)[1]).append(" section during ").append(allfoods.get(i)[2]).append(" time. What are you waiting for? Go before it runs out!");
				}
				else if (i == allfoods.size() - 1) {
					answer.append("as well as at ").append(allfoods.get(i)[0]).append(" in the ").append(allfoods.get(i)[1]).append(" section during ").append(allfoods.get(i)[2]).append(" time. What are you waiting for? Go before it runs out!");
				}
				else {
					answer.append(allfoods.get(i)[0]).append(" in the ").append(allfoods.get(i)[1]).append(" section during ").append(allfoods.get(i)[2]).append(" time, ");
				}
			}
			return answer;
		}
		return new StringBuilder("I am so sorry to report that I have not found your favorite food today during any of the 4 main dining commons :(((. " +
				"However, I am sure the UMass award winning dining commons have more amazing options for you to enjoy! Please search " +
				"again for another another of your favorite food, I am sure UMass is serving it somewhere.");
	}
}

