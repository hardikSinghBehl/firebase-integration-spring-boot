package com.behl.flare.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.flare.dto.MovieDto;
import com.behl.flare.entity.Movie;
import com.behl.flare.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping(value = "/movie")
public class MovieController {

	private final MovieService movieService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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

	@PutMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Returns Updated Movie Document Corresponding To Provided Id")
	public ResponseEntity<?> movieUpdationHandler(@PathVariable(name = "movieId", required = true) final String movieId,
			@Valid @RequestBody(required = true) final Movie movie) {
		return movieService.update(movieId, movie);
	}

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Returns list of all movies")
	public ResponseEntity<List<MovieDto>> moviesRetreivalHandler() {
		return ResponseEntity.ok(movieService.retreiveAll());
	}

	@DeleteMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Deletes movie and corresponding directorMovieMapping documents")
	public ResponseEntity<?> movieDocumentDeletionHandler(
			@PathVariable(required = true, name = "movieId") final String movieId) {
		movieService.delete(movieId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}