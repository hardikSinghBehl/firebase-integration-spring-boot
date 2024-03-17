package com.behl.flare.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Entity {

	DIRECTOR("directors"), MOVIE("movies"), DIRECTOR_MOVIE_MAPPING("director_movie_mappings");

	private final String name;

}
