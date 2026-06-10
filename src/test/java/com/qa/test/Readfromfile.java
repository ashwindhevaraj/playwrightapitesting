package com.qa.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public class Readfromfile {
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
		//byte[] filedata=null;
		//File file=new File("");
		//filedata=Files.readAllBytes(file.toPath());
		
		String jsonBody = Files.readString(
                Paths.get(".//src//test//java//com//qa//test//text1.json"));
		//String body1 = Files.readString(Paths.get(".//src/test//java//com//qa//test//text1.json"));
		//System.out.println(body1);
		APIResponse apiresponse = context.post("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setHeader("Accept","application/json")
				.setHeader("Authorization", "Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8")
                .setData(jsonBody));
		//verification of response body
		//Map<String,String> res=new HashMap<String,String>();
		ObjectMapper obj=new ObjectMapper();
		JsonNode node= obj.readTree(apiresponse.body());
		String body=node.toPrettyString();
		System.out.println(body);
		
		//verification of created post call through get method
		String userid=node.get("id").toString();
		APIResponse getresponse =context.get("https://gorest.co.in/public/v2/users/"+userid,
				RequestOptions.create()
				.setHeader("Authorization","Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8" ));
		Assert.assertEquals(200, getresponse.status());
		Assert.assertEquals("OK", getresponse.statusText());
		Assert.assertTrue(getresponse.text().contains(userid));
		//Assert.assertTrue(getresponse.text().contains("Testashuser"));
		//Assert.assertTrue(getresponse.text().contains(email));
		System.out.println(getresponse.text());

}
	@AfterTest
	public void teardown() {
		playwright.close();
	}

}
