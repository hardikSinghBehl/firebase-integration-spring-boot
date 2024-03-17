package com.behl.flare.client;

import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.behl.flare.configuration.FirebaseConfigurationProperties;
import com.behl.flare.dto.TokenSuccessResponseDto;
import com.behl.flare.dto.UserLoginRequestDto;
import com.behl.flare.exception.InvalidLoginCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(FirebaseConfigurationProperties.class)
public class FirebaseAuthClient {

	private final FirebaseConfigurationProperties firebaseConfigurationProperties;

	private static final String BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
	private static final String ACCESS_TOKEN_KEY = "idToken";
	private static final String API_KEY_PARAM = "key";
	private static final String INVALID_CREDENTIALS_ERROR = "INVALID_LOGIN_CREDENTIALS";
	private static final String SECURE_TOKEN_FIELD = "returnSecureToken";
    
	@SneakyThrows
	@SuppressWarnings("unchecked")
	public TokenSuccessResponseDto login(@NonNull final UserLoginRequestDto userLoginRequest) {
		final var webApiKey = firebaseConfigurationProperties.getFirebase().getWebApiKey();
		final var requestBody = prepareRequestBody(userLoginRequest);
		final Map<String, String> response;
		try {
			response = RestClient.create(BASE_URL)
							.post()
							.uri(uriBuilder -> uriBuilder
									.queryParam(API_KEY_PARAM, webApiKey)
									.build())
							.body(requestBody)
							.contentType(MediaType.APPLICATION_JSON)
							.retrieve()
							.body(Map.class);
		} catch (HttpClientErrorException exception) {
			if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST)
					&& exception.getResponseBodyAsString().contains(INVALID_CREDENTIALS_ERROR)) {
				throw new InvalidLoginCredentialsException();	
			}
			throw exception;
		}
		final var accessToken = response.get(ACCESS_TOKEN_KEY);
		return TokenSuccessResponseDto.builder()
				.accessToken(accessToken)
				.build();
	}
	
	@SneakyThrows
	private String prepareRequestBody(@NonNull final UserLoginRequestDto userLoginRequest) {
		final var requestBody = new ObjectMapper().writeValueAsString(userLoginRequest);
		return new JSONObject(requestBody)
				.put(SECURE_TOKEN_FIELD, true)
				.toString();
	}

}
