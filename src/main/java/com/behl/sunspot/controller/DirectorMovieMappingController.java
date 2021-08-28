package com.behl.sunspot.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.behl.sunspot.entity.DirectorMovieMapping;
import com.behl.sunspot.entity.Movie;
import com.behl.sunspot.service.DirectorMovieMappingService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping(value = "/movie-director-mapping")
@Slf4j
public class DirectorMovieMappingController {

	private final DirectorMovieMappingService directorMovieMappingService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Maps The Director And Movie In The Firebase Collection")
	public ResponseEntity<?> directorMovieMappingCreationHandler(
			@Valid @RequestBody(required = true) final DirectorMovieMapping directorMovieMapping) throws JSONException {
		return directorMovieMappingService.createMapping(directorMovieMapping);
	}

	@GetMapping(value = "/{directorId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Returns List of Movies Directed By The Provided Director")
	public ResponseEntity<List<Movie>> getMoviesByDirectorId(
			@PathVariable(required = true, name = "directorId") final String directorId) {
		try {
			return ResponseEntity.ok(directorMovieMappingService.retreiveMoviesByDirectorId(directorId));
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unable to retrieve movies of director with id {}: {}", directorId, e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/{mappingId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Deletes Specified Mapping Of Movie And Director")
	public ResponseEntity<?> directorMovieMappingDeletionHandler(
			@PathVariable(required = true, name = "mappingId") final String mappingId) {
		return directorMovieMappingService.delete(mappingId);
	}

}