package com.behl.sunspot.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.behl.sunspot.dto.DirectorDto;
import com.behl.sunspot.entity.Director;
import com.behl.sunspot.service.DirectorService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping(value = "/director")
public class DirectorController {

	private final DirectorService directorService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates A Director Document Inside Firebase And Returns It's Id")
	public ResponseEntity<?> directorCreationHandler(@Valid @RequestBody(required = true) final Director director) {
		return directorService.createDirector(director);
	}

	@GetMapping(value = "/{directorId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Returns A Director Document Corresponding To Providied Id")
	public ResponseEntity<Director> directorRetreivalHandler(
			@PathVariable(name = "directorId", required = true) final String directorId) {
		return ResponseEntity.ok(directorService.retreive(directorId));
	}

	@PutMapping(value = "/{directorId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Returns Updated Director Document Corresponding To Provided Id")
	public ResponseEntity<?> directorUpdationHandler(
			@PathVariable(name = "directorId", required = true) final String directorId,
			@Valid @RequestBody(required = true) final Director director) {
		return directorService.update(directorId, director);
	}

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Returns list of all directors")
	public ResponseEntity<List<DirectorDto>> directorsRetreivalHandler() {
		return ResponseEntity.ok(directorService.retreiveAll());
	}

	@DeleteMapping(value = "/{directorId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Deletes director and corresponding directorMovieMapping documents")
	public ResponseEntity<?> directorDocumentDeletionHandler(
			@PathVariable(required = true, name = "directorId") final String directorId) {
		directorService.delete(directorId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}