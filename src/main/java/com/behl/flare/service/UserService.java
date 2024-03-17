package com.behl.flare.service;

import org.springframework.stereotype.Service;

import com.behl.flare.client.FirebaseAuthClient;
import com.behl.flare.dto.TokenSuccessResponseDto;
import com.behl.flare.dto.UserCreationRequestDto;
import com.behl.flare.dto.UserLoginRequestDto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord.CreateRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class UserService {

	private final FirebaseAuth firebaseAuth;
	private final FirebaseAuthClient firebaseAuthClient;

	@SneakyThrows
	public void create(@NonNull final UserCreationRequestDto userCreationRequest) {
		final var request = new CreateRequest();
		request.setEmail(userCreationRequest.getEmail());
		request.setPassword(userCreationRequest.getPassword());
		request.setEmailVerified(Boolean.TRUE);

		firebaseAuth.createUser(request);
	}
	
	public TokenSuccessResponseDto login(@NonNull final UserLoginRequestDto userLoginRequest) {
		return firebaseAuthClient.login(userLoginRequest);
	}

}
