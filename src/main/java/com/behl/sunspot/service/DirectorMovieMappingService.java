package com.behl.sunspot.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.behl.sunspot.constant.Entity;
import com.behl.sunspot.entity.DirectorMovieMapping;
import com.behl.sunspot.entity.Movie;
import com.google.cloud.firestore.Firestore;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DirectorMovieMappingService {

	private final Firestore firestore;

	public List<Movie> retreiveMoviesByDirectorId(final String directorId)
			throws InterruptedException, ExecutionException {

		final var mappingList = firestore.collection(Entity.DIRECTOR_MOVIE_MAPPING.getName())
				.whereEqualTo("directorId", directorId).get().get().getDocuments();

		return mappingList.stream().map(mapping -> {
			final var directorMovieMapping = mapping.toObject(DirectorMovieMapping.class);

			try {
				return firestore.collection(Entity.MOVIE.getName()).document(directorMovieMapping.getMovieId()).get()
						.get().toObject(Movie.class);
			} catch (InterruptedException | ExecutionException e) {
				return null;
			}
		}).collect(Collectors.toList());
	}

	public ResponseEntity<?> createMapping(final DirectorMovieMapping directorMovieMapping) throws JSONException {
		final var response = new JSONObject();
		final var mappingId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		firestore.collection(Entity.DIRECTOR_MOVIE_MAPPING.getName()).document(mappingId).set(directorMovieMapping);

		response.put("id", mappingId);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> delete(String mappingId) throws JSONException {
		final var response = new JSONObject();

		firestore.collection(Entity.DIRECTOR_MOVIE_MAPPING.getName()).document(mappingId).delete();

		response.put("message", "Mapping Id: " + mappingId + " Successfully Deleted");
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.ok(response.toString());
	}

}