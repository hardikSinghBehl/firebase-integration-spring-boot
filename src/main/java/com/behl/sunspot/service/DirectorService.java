package com.behl.sunspot.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.behl.sunspot.entity.Director;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DirectorService {

	private final Firestore firestore;

	public ResponseEntity<?> createDirector(Director director) throws JSONException {
		final var response = new JSONObject();
		final var directorId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		firestore.collection("directors").document(directorId).set(director);

		response.put("id", directorId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public Director retreive(String directorId) throws InterruptedException, ExecutionException {
		DocumentSnapshot retrievedDirector = firestore.collection("directors").document(directorId).get().get();
		return retrievedDirector.exists() ? retrievedDirector.toObject(Director.class) : null;
	}

	public ResponseEntity<?> update(String directorId, Director director) throws JSONException {
		final var response = new JSONObject();

		firestore.collection("directors").document(directorId).set(director);

		response.put("id", directorId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

}