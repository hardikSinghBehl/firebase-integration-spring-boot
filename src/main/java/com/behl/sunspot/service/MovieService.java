package com.behl.sunspot.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.behl.sunspot.entity.Movie;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MovieService {

	private final Firestore firestore;

	public Movie retreive(final String movieId) throws InterruptedException, ExecutionException {
		DocumentSnapshot retrievedMovie = firestore.collection("movies").document(movieId).get().get();
		return retrievedMovie.exists() ? retrievedMovie.toObject(Movie.class) : null;
	}

	public ResponseEntity<?> createMovie(final Movie movie) throws JSONException {
		final var response = new JSONObject();
		final var movieId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		firestore.collection("movies").document(movieId).set(movie);

		response.put("id", movieId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> update(String movieId, Movie movie) throws JSONException {
		final var response = new JSONObject();

		firestore.collection("movies").document(movieId).set(movie);

		response.put("id", movieId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

}