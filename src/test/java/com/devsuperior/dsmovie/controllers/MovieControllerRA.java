package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {
	
	private String clientUsername, clientPassword;

	private String adminUsername, adminPassword;

	private String adminToken, clientToken, invalidToken;
	
	private String validMovieTitle;
	
	@BeforeEach
	public void setUp() throws Exception{
		
		baseURI = "http://localhost:8080";
		
		clientUsername = "alex@gmail.com";
		
		adminUsername = "maria@gmail.com";
		
		clientPassword = "123456";
		
		adminPassword = "123456";

		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		
		invalidToken = adminToken + "xpto";
		
		validMovieTitle = "Carnificina";
	}
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		given()
			.get("/movies")
		.then()
			.statusCode(200);
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {		
		given()
			.get("/movies?title={title}", validMovieTitle)
		.then()
			.statusCode(200)
			.body("content.id[0]", is(2))
			.body("content.title[0]", equalTo("Venom: Tempo de Carnificina"));		
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {	
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
