package com.behl.flare.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class Movie {

	@NotBlank(message = "movieName must not be blank")
	@Size(max = 50, message = "movieName must not exceed 50 characters")
	private String name;

	@NotNull(message = "durationInMinutes must not be null")
	private Integer durationInMinutes;

}