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

public class Reusegeneratetoken {

	static Playwright playwright;
	static APIRequest apirequest;
	static APIRequestContext context;
	public static String TOKEN=null;
	@BeforeTest
	public void setup() throws IOException {
		playwright = Playwright.create();
		apirequest = playwright.request();
		context = apirequest.newContext();
		Map<String,String> data=new HashMap<String,String>();
		data.put("username","admin");
		data.put("password","password123");
		
		APIResponse apiresponse = context.post("https://restful-booker.herokuapp.com/auth",
				RequestOptions.create().setHeader("Content-Type","application/json").setData(data));
		ObjectMapper obj=new ObjectMapper();
		JsonNode node=obj.readTree(apiresponse.body());
		
		//verification of response
		TOKEN = node.get("token").toString();
		System.out.println(TOKEN);
	}
	@Test(priority=1)
	public void requesttwochecktest() throws IOException {
		String data1 ="{\r\n"
				+ "    \"firstname\" : \"James\",\r\n"
				+ "    \"lastname\" : \"Brown\",\r\n"
				+ "    \"totalprice\" : 111,\r\n"
				+ "    \"depositpaid\" : true,\r\n"
				+ "    \"bookingdates\" : {\r\n"
				+ "        \"checkin\" : \"2018-01-01\",\r\n"
				+ "        \"checkout\" : \"2019-01-01\"\r\n"
				+ "    },\r\n"
				+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
				+ "}";
		APIResponse apiresponse1 = context.put("https://restful-booker.herokuapp.com/booking/1",
				RequestOptions.create().setHeader("Content-Type","application/json")
				.setHeader("Accept","application/json")
				.setHeader("Cookie","token="+TOKEN)
				.setData(data1));
		
		//Deserialization zone
		String apiresponsestring = apiresponse1.text();
		ObjectMapper obj1=new ObjectMapper();
		JsonNode node1=obj1.readTree(apiresponse1.body());
		String finalres = node1.toPrettyString();
		
		
		System.out.println(finalres);
		
}
	@Test(priority=2)
	public void deletetest() {
		APIResponse deleteres	 = context.delete("https://restful-booker.herokuapp.com/booking/1",
				RequestOptions.create().setHeader("Content-Type","application/json")
				.setHeader("Cookie","token="+TOKEN));
		Assert.assertEquals(deleteres.status(),201);
	}
	@AfterTest
	public void teardown() {
		playwright.close();
	}
}
