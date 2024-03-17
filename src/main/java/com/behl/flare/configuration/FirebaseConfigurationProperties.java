package com.behl.flare.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "com.behl.flare")
public class FirebaseConfigurationProperties {

	@Valid
	private FireBase firebase = new FireBase();

	@Getter
	@Setter
	public class FireBase {

		@NotBlank
		private String privateKey;
		
		@NotBlank
		private String webApiKey;
		
	}

}