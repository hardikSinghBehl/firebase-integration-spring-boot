package com.behl.flare.service;

import org.springframework.stereotype.Service;

import com.behl.flare.client.FirebaseAuthClient;
import com.behl.flare.dto.TokenSuccessResponseDto;
import com.behl.flare.dto.UserCreationRequestDto;
import com.behl.flare.dto.UserLoginRequestDto;
import com.behl.flare.exception.AccountAlreadyExistsException;
import com.behl.flare.exception.InvalidLoginCredentialsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord.CreateRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class UserService {

	private final FirebaseAuth firebaseAuth;
	private final FirebaseAuthClient firebaseAuthClient;

	/**
	 * Creates a new user record in the system corresponding to provided 
	 * creation request details.
	 * 
	 * @param userCreationRequest the request containing user creation details
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws AccountAlreadyExistsException If an account with the provided email-id already exists.
	 */
	@SneakyThrows
	public void create(@NonNull final UserCreationRequestDto userCreationRequest) {		
		final var request = new CreateRequest();
		request.setEmail(userCreationRequest.getEmailId());
		request.setPassword(userCreationRequest.getPassword());
		request.setEmailVerified(Boolean.TRUE);

		try {
			firebaseAuth.createUser(request);
		} catch (final FirebaseAuthException exception) {
			if (exception.getMessage().contains("EMAIL_EXISTS")) {
				throw new AccountAlreadyExistsException("Account with provided email-id already exists");
			}
			throw exception;
		}
	}
	
	/**
	 * Validates user login credentials and generates an access token on successful
	 * authentication.
	 * 
	 * @param userLoginRequest the request containing user login details
	 * @return a TokenSuccessResponseDto containing the access token
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws InvalidLoginCredentialsException If the provided login credentials are invalid.
	 */
	public TokenSuccessResponseDto login(@NonNull final UserLoginRequestDto userLoginRequest) {
		return firebaseAuthClient.login(userLoginRequest);
	}

}
