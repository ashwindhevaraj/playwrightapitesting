package com.qa.Delete;

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

public class Deletevalidationtest {
	//class 9- first post call - second delete call -third get call -to verify the deletion happens or not
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
			
			// Delete request call
			String userid=respuser.getId(); // getting id from previous post call here
			 
			APIResponse apidelresponse = context.delete("https://gorest.co.in/public/v2/users/"+userid,
					RequestOptions.create().setHeader("Accept","application/json")
					.setHeader("Authorization", "Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8"));
			System.out.println("-------------------------------------");
			//Deserialization part after sending put request call
			
					String delresponstring = apidelresponse.text();
					System.out.println("printing body after delete request made ");
					System.out.println(delresponstring);
					System.out.println(apidelresponse.status());
					System.out.println(apidelresponse.statusText());
					
			Assert.assertEquals(apidelresponse.status(), 204);
			//Assert.assertEquals(apidelresponse.statusText(), "NOT FOUND");
			
			// get call to make sure deleted data should not be reflected
			
			APIResponse apigetresponse = context.get("https://gorest.co.in/public/v2/users/"+userid,
					RequestOptions.create().setHeader("Accept","application/json")
					.setHeader("Authorization", "Bearer e0c6e96fe92eebe84c94e7f8e67fbc7393aed986dea991b02974902c102e39d8"));
			String finalgetres = apigetresponse.text();
			//ObjectMapper obj2=new ObjectMapper();
			//Users getresuser=obj2.readValue(finalgetres,Users.class);
			Assert.assertEquals(apigetresponse.status(), 404);
			Assert.assertEquals(apigetresponse.statusText(), "Not Found");
			System.out.println(finalgetres);
			Assert.assertTrue(finalgetres.contains("Resource not found"));
			
	}
		@AfterTest
		public void teardown() {
			playwright.close();
		}
		public static String randomemail() {
			return "random"+System.currentTimeMillis()+"@gmail.com";
		}
}
