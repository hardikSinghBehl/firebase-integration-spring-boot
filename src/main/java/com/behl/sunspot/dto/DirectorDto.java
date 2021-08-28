package com.behl.sunspot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DirectorDto {

	private String id;
	private String firstName;
	private String lastName;
	private String country;

}
