package com.behl.flare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "UserCreationRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class UserCreationRequestDto {

	@NotBlank(message = "email must not be empty")
	@Email(message = "email must be of valid format")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "email-id of user", example = "hardik.behl7444@gmail.com")
	private String email;
	
	@NotBlank(message = "password must not be empty")
	@Size(min = 6, message = "password length must be 6 characters long")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "secure password to enable user login", example = "somethingSecure")
	private String password;

}