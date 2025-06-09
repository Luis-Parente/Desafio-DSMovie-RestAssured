package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;
import java.util.HashMap;

public class MovieControllerRA {
	
	private String clientUsername, clientPassword;

	private String adminUsername, adminPassword;

	private String adminToken, clientToken, invalidToken;
	
	private String validMovieTitle;
	
	private Long existingMovieId, nonExistingMovieId;
	
	private Map<String, Object> postMovieInstance;
	
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
		
		existingMovieId = 1L;
		
		nonExistingMovieId = 100L;
		
		postMovieInstance = new HashMap<>();
		
		postMovieInstance.put("title", "Test Movie");
		postMovieInstance.put("score", 0.0);
		postMovieInstance.put("count", 0);
		postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
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
		given()
			.get("/movies/{id}", existingMovieId)
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("title", equalTo("The Witcher"))
			.body("score", is(4.5F))
			.body("count", is(2))
			.body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {	
		given()
			.get("/movies/{id}", nonExistingMovieId)
		.then()
			.statusCode(404);
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
		postMovieInstance.put("title", " ");
		
		JSONObject newMovie = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newMovie)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(422);
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		JSONObject newMovie = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newMovie)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(403);
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		JSONObject newMovie = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.body(newMovie)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(401);
	}
}
