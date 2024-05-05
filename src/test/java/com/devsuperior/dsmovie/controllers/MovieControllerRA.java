package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


public class MovieControllerRA {

	private String movieTitle;
	private Long existingMovieId, nonExistingMovieId;

	@BeforeEach
	public void setUp(){
		baseURI = "http://localhost:8080";

		movieTitle = "Venom: Tempo de Carnificina";
		existingMovieId = 1L;
		nonExistingMovieId = 50L;

	}

	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {

		given()
				.get("/movies?page=0")
		.then()
				.statusCode(200)
				.body("content[0].id", is(1))
				.body("content[0].title", equalTo("The Witcher"))
				.body("content[0].score", is(4.5F))
				.body("content[0].count", is(2))
				.body("content[0].image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));

	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {

		given()
				.get("/movies?title={movieTitle}", movieTitle)
		.then()
				.statusCode(200)
				.body("content[0].id", is(2))
				.body("content[0].title", equalTo("Venom: Tempo de Carnificina"))
				.body("content[0].score", is(3.3F))
				.body("content[0].count", is(3))
				.body("content[0].image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg"));

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
				.statusCode(404)
				.body("status", is(404))
				.body("error", equalTo("Recurso não encontrado"));

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
