package com.behl.sunspot.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class Director {

	@NotBlank(message = "firstName must not be blank")
	@Size(max = 50, message = "firstname must not exceed 50 characters")
	private String firstName;

	@NotBlank(message = "lastName must not be blank")
	@Size(max = 50, message = "lastName must not exceed 50 characters")
	private String lastName;

	@NotBlank(message = "countryName must not be blank")
	@Size(max = 50, message = "countryName must not exceed 50 characters")
	private String country;

}