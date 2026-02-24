package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmpControllerTest {
	
	private static HttpClient client = HttpClient.newHttpClient();
	
	private static final String base = "http://localhost:8080/employees/";
	
	@Test
	@DisplayName("200 Response")
	void testGetEmployeesInt() throws Exception {
		
		int tempEmpID = 11;
		String url = base + tempEmpID;
		
		HttpRequest request  = HttpRequest.newBuilder().uri(new URI(url)).build();
		HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		assertEquals(200, response.statusCode());

	}

	@Test
	@DisplayName("204 Response")
	void testGetEmployeesNotFound() throws Exception {
		
		int tempEmpID = 10;
		String url = base + tempEmpID;
		
		HttpRequest request  = HttpRequest.newBuilder().uri(new URI(url)).build();
		HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		assertEquals(204, response.statusCode());
	}
	
}
