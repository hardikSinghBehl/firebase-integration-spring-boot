package com.behl.flare.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Maps configuration values defined in the active {@code .yml} file to the
 * corresponding variables below. The configuration properties are referenced
 * within the application to facilitate connection with Firebase services.
 * 
 * @see FirebaseConfiguration
 */
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

		/**
		 * The configuration file contents of the service account to be used to
		 * communicate with Firestore database.
		 */
		@NotBlank(message = "Firestore private key must be configured")
		private String privateKey;
		
		/**
		 * Web API key to be used to communicate with Firebase Authentication service
		 * via REST API. This is required token exchange with email/password is not
		 * supported natively by the Firebase admin sdk.
		 * 
		 * @see com.behl.flare.client.FirebaseAuthClient
		 */
		@NotBlank(message = "Firebase Web API key must be configured")
		private String webApiKey;
		
	}

}