package com.qa.token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qa.main.Users;

public class Tokencreationtest {

	Playwright playwright;
	APIRequest apirequest;
	APIRequestContext context;
	@BeforeTest
	public void setup() {
		playwright = Playwright.create();
		apirequest = playwright.request();
		context = apirequest.newContext();
	}
	@Test
	public void requesttwochecktest() throws IOException {
		Map<String,String> data=new HashMap<String,String>();
		data.put("username","admin");
		data.put("password","password123");
		
		APIResponse apiresponse = context.post("https://restful-booker.herokuapp.com/auth",
				RequestOptions.create().setHeader("Content-Type","application/json").setData(data));
		
		//Deserialization zone
		String apiresponsestring = apiresponse.text();
		ObjectMapper obj=new ObjectMapper();
		JsonNode node=obj.readTree(apiresponse.body());
		String finalres = node.toPrettyString();
		
		//verification of response
		String token = node.get("token").toString();
		System.out.println(token);
		System.out.println(finalres);
		
		Assert.assertNotNull(token);
}
	@AfterTest
	public void teardown() {
		playwright.close();
	}
}

