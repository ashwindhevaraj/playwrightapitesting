package com.qa.put;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qa.main.Users;

public class Putrequestvalidations {
//class 8- first post call - second put call -third get call -to verify the updation happens or not
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
		Users users=Users.builder()
				.name("newusertest1")
				.email(randomemail())
				.gender("male")
				.status("active").build();
		
		APIResponse apiresponse = context.post("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setHeader("Accept","application/json")
				.setHeader("Authorization", "Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8")
.setData(users)); //serialization not needed - set data will handle inbuilt conversion of java object to json
		
		//Deserialization part
		
		String apiresponsestring = apiresponse.text();
		System.out.println(apiresponsestring);
		ObjectMapper obj=new ObjectMapper();
		Users respuser=obj.readValue(apiresponsestring,Users.class); //readvalue converts json to java object
		
		// Put request call
		users.setName("newusertest2");
		//users.setGender("female");
		users.setStatus("inactive");
		String userid=respuser.getId();
		
		APIResponse apiputresponse = context.put("https://gorest.co.in/public/v2/users/"+userid,
				RequestOptions.create().setHeader("Accept","application/json")
				.setHeader("Authorization", "Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8")
.setData(users));
		System.out.println("-------------------------------------");
		//Deserialization part after sending put request call
		
				String apiputresponsestring = apiputresponse.text();
				System.out.println(apiputresponsestring);
				ObjectMapper obj1=new ObjectMapper();
				Users putresuser=obj1.readValue(apiputresponsestring,Users.class); //readvalue converts json to java object
		
		Assert.assertEquals(users.getEmail(), putresuser.getEmail());
		Assert.assertEquals(users.getName(), putresuser.getName());
		Assert.assertEquals(users.getGender(),putresuser.getGender());
		Assert.assertEquals(users.getStatus(),putresuser.getStatus());
		Assert.assertNotNull(putresuser.getId());
		
		// get call to make sure updated data getting reflected or not
		
		APIResponse apigetresponse = context.get("https://gorest.co.in/public/v2/users/"+userid,
				RequestOptions.create().setHeader("Accept","application/json")
				.setHeader("Authorization", "Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8"));
		String finalgetres = apigetresponse.text();
		ObjectMapper obj2=new ObjectMapper();
		Users getresuser=obj2.readValue(finalgetres,Users.class);
		Assert.assertEquals(getresuser.getName(), "newusertest2");
		Assert.assertEquals(getresuser.getStatus(), "inactive");
		
}
	@AfterTest
	public void teardown() {
		playwright.close();
	}
	public static String randomemail() {
		return "random"+System.currentTimeMillis()+"@gmail.com";
	}
}
