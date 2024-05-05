package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ScoreControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;
	private Map<String, Object> postScoreInstance;


	@BeforeEach
	public void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";

		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", 1);
		postScoreInstance.put("score", 4.0);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {

		postScoreInstance.put("movieId", 50);
		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
		.when()
				.put("/scores")
		.then()
				.statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {

		postScoreInstance.put("movieId", "");
		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("error", equalTo("Dados inválidos"));

	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {

		postScoreInstance.put("score", -10.0);
		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("error", equalTo("Dados inválidos"));

	}
}
