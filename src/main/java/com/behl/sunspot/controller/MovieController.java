package com.behl.sunspot.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.sunspot.entity.Movie;
import com.behl.sunspot.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping(value = "/movie")
public class MovieController {

	private final MovieService movieService;

	@PostMapping
	@Operation(summary = "Creates A Movie Document Inside Firebase And Returns It's Id")
	public ResponseEntity<?> movieCreationHandler(@Valid @RequestBody(required = true) final Movie movieId) {
		return movieService.createMovie(movieId);
	}

	@GetMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Returns A Movie Document Corresponding To Providied Id")
	public ResponseEntity<Movie> movieRetreivalHandler(
			@PathVariable(name = "movieId", required = true) final String movieId) {
		return ResponseEntity.ok(movieService.retreive(movieId));
	}

	@PutMapping(value = "/{movieId}")
	@Operation(summary = "Returns Updated Movie Document Corresponding To Provided Id")
	public ResponseEntity<?> movieUpdationHandler(@PathVariable(name = "movieId", required = true) final String movieId,
			@Valid @RequestBody(required = true) final Movie movie) {
		return movieService.update(movieId, movie);
	}

}