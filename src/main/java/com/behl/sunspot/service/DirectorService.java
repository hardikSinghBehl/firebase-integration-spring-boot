package com.behl.sunspot.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.behl.sunspot.constant.Entity;
import com.behl.sunspot.entity.Director;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DirectorService {

	private final Firestore firestore;

	public Director retreive(final String directorId) throws InterruptedException, ExecutionException {
		DocumentSnapshot retrievedDirector = firestore.collection(Entity.DIRECTOR.getName()).document(directorId).get()
				.get();
		return retrievedDirector.exists() ? retrievedDirector.toObject(Director.class) : null;
	}

	public ResponseEntity<?> createDirector(final Director director) throws JSONException {
		final var response = new JSONObject();
		final var directorId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		firestore.collection(Entity.DIRECTOR.getName()).document(directorId).set(director);

		response.put("id", directorId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> update(final String directorId, final Director director) throws JSONException {
		final var response = new JSONObject();

		firestore.collection(Entity.DIRECTOR.getName()).document(directorId).set(director);

		response.put("id", directorId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

}