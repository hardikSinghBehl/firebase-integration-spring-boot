package com.behl.flare.configuration;

import java.io.FileInputStream;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FirebaseConfigurationProperties.class)
public class FirebaseConfiguration {

	private final FirebaseConfigurationProperties fireBaseConfigurationProperties;

	@Bean
	@SneakyThrows
	public FirebaseApp firebaseApp() {
		final var privateKey = fireBaseConfigurationProperties.getFirebase().getPrivateKey();
		final var serviceAccount = new FileInputStream(privateKey);
		final var firebaseOptions = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		return FirebaseApp.initializeApp(firebaseOptions);
	}

	@Bean
	public Firestore firestore(final FirebaseApp firebaseApp) {
		return FirestoreClient.getFirestore(firebaseApp);
	}
	
	@Bean
	public FirebaseAuth firebaseAuth(final FirebaseApp firebaseApp) {
		return FirebaseAuth.getInstance(firebaseApp);
	}

}