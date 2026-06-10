package com.qa.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;

public class Headerarray {
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
	public void requesttwocheck() throws IOException {
		APIResponse apiresponse = context.get("https://gorest.co.in/public/v2/users");
		//first method
		Map<String,String> headers = apiresponse.headers();
		headers.forEach((k,v)->{
			System.out.println(k+" : "+v );
		});
		//validations in first method
		Assert.assertEquals("SAMEORIGIN",headers.get("x-frame-options"));
		Assert.assertEquals("",headers.get("x-links-previous"));
		
		//second method
		List<HttpHeader> headerlist = apiresponse.headersArray();
		for(HttpHeader e:headerlist) {
			System.out.println(e.name+" : "+e.value);
		}
		System.out.println(headerlist.size());
		
		//validations in second method
		//we cant validate list directly better you can use map method
}
	@AfterTest
	public void teardown() {
		playwright.close();
	}
}

