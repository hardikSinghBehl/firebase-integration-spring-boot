package com.behl.flare.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.behl.flare")
public class FirebaseConfigurationProperties {

	private FireBase firebase = new FireBase();

	@Getter
	@Setter
	public class FireBase {

		private String privateKey;

	}

}