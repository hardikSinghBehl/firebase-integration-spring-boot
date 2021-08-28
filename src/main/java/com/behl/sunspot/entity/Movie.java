package com.behl.sunspot.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Movie {

	@NotBlank(message = "movieName must not be blank")
	@Size(max = 50, message = "movieName must not exceed 50 characters")
	private String name;

	@NotNull(message = "durationInMinutes must not be null")
	private Integer durationInMinutes;

}