package com.behl.flare.firebase.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "com.behl.flare")
@Data
public class FireBaseConfigurationProperties {

	private Configuration firebase = new Configuration();

	@Data
	public class Configuration {

		private String firebasePrivateKey;

	}

}