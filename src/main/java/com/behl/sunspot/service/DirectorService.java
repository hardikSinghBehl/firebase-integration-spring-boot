package com.behl.sunspot.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.behl.sunspot.constant.Entity;
import com.behl.sunspot.dto.DirectorDto;
import com.behl.sunspot.entity.Director;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class DirectorService {

	private final Firestore firestore;

	public Director retreive(final String directorId) {
		DocumentSnapshot retrievedDirector = null;
		try {
			retrievedDirector = firestore.collection(Entity.DIRECTOR.getName()).document(directorId).get().get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unable to retreive director with id {}: {}", directorId, e);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		final var director = retrievedDirector.exists() ? retrievedDirector.toObject(Director.class) : null;
		if (director == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		return director;
	}

	public ResponseEntity<?> createDirector(final Director director) {
		final var response = new JSONObject();
		final var directorId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		firestore.collection(Entity.DIRECTOR.getName()).document(directorId).set(director);

		response.put("id", directorId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> update(final String directorId, final Director director) {
		final var response = new JSONObject();

		firestore.collection(Entity.DIRECTOR.getName()).document(directorId).set(director);

		response.put("id", directorId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public List<DirectorDto> retreiveAll() {
		try {
			return firestore.collection(Entity.DIRECTOR.getName()).get().get().getDocuments().parallelStream()
					.map(director -> {
						final var directorDocument = director.toObject(Director.class);
						return DirectorDto.builder().id(director.getId()).firstName(directorDocument.getFirstName())
								.lastName(directorDocument.getLastName()).country(directorDocument.getCountry())
								.build();
					}).collect(Collectors.toList());
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unable to retrieve all directors from database: {}", e);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
	}

}