package com.behl.sunspot.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.behl.sunspot.constant.Entity;
import com.behl.sunspot.dto.MovieDto;
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
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
		final var movie = retrievedMovie.exists() ? retrievedMovie.toObject(Movie.class) : null;
		if (movie == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		return movie;
	}

	public ResponseEntity<?> createMovie(final Movie movie) {
		final var response = new HashMap<String, Object>();
		final var movieId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		firestore.collection(Entity.MOVIE.getName()).document(movieId).set(movie);

		response.put("id", movieId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> update(final String movieId, final Movie movie) {
		final var response = new HashMap<String, Object>();

		firestore.collection(Entity.MOVIE.getName()).document(movieId).set(movie);

		response.put("id", movieId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public List<MovieDto> retreiveAll() {
		try {
			return firestore.collection(Entity.MOVIE.getName()).get().get().getDocuments().parallelStream()
					.map(movie -> {
						final var movieDocument = movie.toObject(Movie.class);
						return MovieDto.builder().id(movie.getId()).name(movieDocument.getName())
								.durationInMinutes(movieDocument.getDurationInMinutes()).build();
					}).collect(Collectors.toList());
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unable to retrieve all movies from database: {}", e);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
	}

	public void delete(final String movieId) {
		try {
			firestore.collection(Entity.DIRECTOR_MOVIE_MAPPING.getName()).whereEqualTo("movieId", movieId).get().get()
					.getDocuments().forEach(directorMovieMapping -> {
						firestore.collection(Entity.DIRECTOR_MOVIE_MAPPING.getName())
								.document(directorMovieMapping.getId()).delete();
					});
			firestore.collection(Entity.MOVIE.getName()).document(movieId).delete();
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unable to delete directorMovieMapping records for movie: {} from database: {}", movieId, e);
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
		}
	}

}