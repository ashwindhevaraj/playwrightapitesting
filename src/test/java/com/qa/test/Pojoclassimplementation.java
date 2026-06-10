package com.qa.test;

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
import com.qa.main.User;

public class Pojoclassimplementation {
	User user=new User("ashtestuser",randomemail(),"male","active");
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
		APIResponse apiresponse = context.post("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setHeader("Accept","application/json")
				.setHeader("Authorization", "Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8")
.setData(user)); //serialization not needed - set data will handle inbuilt conversion of java object to json
		
		//Deserialization zone
		String apiresponsestring = apiresponse.text();
		ObjectMapper obj=new ObjectMapper();
		User respuser=obj.readValue(apiresponsestring,User.class); //readvalue converts json to java object
		
		//verification of returned pojo class
		System.out.println(apiresponsestring); //printing output
		
		Assert.assertEquals(user.getEmail(), respuser.getEmail());
		Assert.assertEquals(user.getName(), respuser.getName());
		Assert.assertEquals(user.getGender(),respuser.getGender());
		Assert.assertEquals(user.getStatus(),respuser.getStatus());
		Assert.assertNotNull(respuser.getId());
}
	@AfterTest
	public void teardown() {
		playwright.close();
	}
	public static String randomemail() {
		return "random"+System.currentTimeMillis()+"@gmail.com";
	}

}
