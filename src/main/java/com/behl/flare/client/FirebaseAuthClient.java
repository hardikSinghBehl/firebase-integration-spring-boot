package com.behl.flare.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.behl.flare.configuration.FirebaseConfigurationProperties;
import com.behl.flare.dto.FirebaseSignInRequestDto;
import com.behl.flare.dto.FirebaseSignInResponseDto;
import com.behl.flare.dto.TokenSuccessResponseDto;
import com.behl.flare.dto.UserLoginRequestDto;
import com.behl.flare.exception.InvalidLoginCredentialsException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(FirebaseConfigurationProperties.class)
public class FirebaseAuthClient {

	private final FirebaseConfigurationProperties firebaseConfigurationProperties;

	private static final String BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
	private static final String API_KEY_PARAM = "key";
	private static final String INVALID_CREDENTIALS_ERROR = "INVALID_LOGIN_CREDENTIALS";
    
	public TokenSuccessResponseDto login(@NonNull final UserLoginRequestDto userLoginRequest) {
		final var requestBody = prepareRequestBody(userLoginRequest);
		final var response = sendSignInRequest(requestBody);
		return TokenSuccessResponseDto.builder()
				.accessToken(response.getIdToken())
				.build();
	}
	
	private FirebaseSignInResponseDto sendSignInRequest(@NonNull final FirebaseSignInRequestDto request) {
		final var webApiKey = firebaseConfigurationProperties.getFirebase().getWebApiKey();
		final FirebaseSignInResponseDto response;
		try {
			response = RestClient.create(BASE_URL)
					.post()
					.uri(uriBuilder -> uriBuilder
							.queryParam(API_KEY_PARAM, webApiKey)
							.build())
					.body(request)
					.contentType(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(FirebaseSignInResponseDto.class);
		} catch (HttpClientErrorException exception) {
			if (exception.getResponseBodyAsString().contains(INVALID_CREDENTIALS_ERROR)) {
				throw new InvalidLoginCredentialsException();	
			}
			throw exception;
		}
		return response;
	}
	
	private FirebaseSignInRequestDto prepareRequestBody(@NonNull final UserLoginRequestDto userLoginRequest) {
		final var request = new FirebaseSignInRequestDto();
		request.setEmail(userLoginRequest.getEmailId());
		request.setPassword(userLoginRequest.getPassword());
		request.setReturnSecureToken(Boolean.TRUE);
		return request;
	}

}
