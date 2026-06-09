package com.qa.test;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class Getrequestsample {
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
	public void getRequestcheck1()throws IOException {
		//hitting get request here
		APIResponse apiresponse = context.get("https://gorest.co.in/public/v2/users");
		int statuscode = apiresponse.status();
		//printing status code- it returns 200
		System.out.println(statuscode);
		String statustext = apiresponse.statusText();
		//printing status text alone- it returns OK
		System.out.println(statustext);
		// jackson parser used to convert object type to json type
		ObjectMapper objmapper= new ObjectMapper();
		JsonNode jsonresponse = objmapper.readTree(apiresponse.body());
		String jsonprettyresponse=jsonresponse.toPrettyString();
		System.out.println(jsonprettyresponse);
		
		//validating response headers
		System.out.println("-----printing API response URL------");
		System.out.println(apiresponse.url());
		
		//printing response headers
		Map<String,String> resheaders=apiresponse.headers();
		System.out.println(resheaders);
		
		//validating response headers
		Assert.assertEquals(resheaders.get("content-type"),"application/json; charset=utf-8");
		Assert.assertEquals(resheaders.get("transfer-encoding"),"chunked");
		Assert.assertEquals(resheaders.get("connection"),"keep-alive");;
	}
	@Test
	public void queryparametercheck() throws IOException{
		APIResponse apiresponse = context.get("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setQueryParam("id", "8496203").setQueryParam("status", "inactive"));
		
		int statuscode = apiresponse.status();
		//printing status code- it returns 200
		System.out.println(statuscode);
		String statustext = apiresponse.statusText();
		//printing status text alone- it returns OK
		System.out.println(statustext);
		// jackson parser used to convert object type to json type
		ObjectMapper objmapper= new ObjectMapper();
		JsonNode jsonresponse = objmapper.readTree(apiresponse.body());
		String jsonprettyresponse=jsonresponse.toPrettyString();
		System.out.println(jsonprettyresponse);
	}

}
