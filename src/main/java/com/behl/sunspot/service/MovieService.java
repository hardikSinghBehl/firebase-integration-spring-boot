package com.behl.sunspot.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.behl.sunspot.constant.Entity;
import com.behl.sunspot.entity.Movie;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MovieService {

	private final Firestore firestore;

	public Movie retreive(final String movieId) {
		DocumentSnapshot retrievedMovie = null;
		try {
			retrievedMovie = firestore.collection(Entity.MOVIE.getName()).document(movieId).get().get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unable to retreive movie with id {}: {}", movieId, e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		final var movie = retrievedMovie.exists() ? retrievedMovie.toObject(Movie.class) : null;
		if (movie == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		return movie;
	}

	public ResponseEntity<?> createMovie(final Movie movie) {
		final var response = new JSONObject();
		final var movieId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		firestore.collection(Entity.MOVIE.getName()).document(movieId).set(movie);

		response.put("id", movieId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> update(final String movieId, final Movie movie) {
		final var response = new JSONObject();

		firestore.collection(Entity.MOVIE.getName()).document(movieId).set(movie);

		response.put("id", movieId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

}