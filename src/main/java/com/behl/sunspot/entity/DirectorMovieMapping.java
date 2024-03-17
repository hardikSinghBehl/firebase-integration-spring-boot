package com.behl.sunspot.entity;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DirectorMovieMapping {

	@NotBlank(message = "directorId must not be blank")
	private String directorId;

	@NotBlank(message = "movieId must not be blank")
	private String movieId;

}