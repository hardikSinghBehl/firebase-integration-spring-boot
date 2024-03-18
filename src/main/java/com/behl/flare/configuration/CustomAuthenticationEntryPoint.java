package com.behl.flare.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.behl.flare.exception.TokenVerificationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Custom implementation of Spring Security's AuthenticationEntryPoint interface
 * which is responsible for handling authentication exceptions that occur during
 * evaluation of security filter chains. 
 * 
 * This implementation assumes any exception thrown by the custom filter is due
 * to token verification failure and hence passes a new instance of
 * {@link com.behl.flare.exception.TokenVerificationException} which is caught
 * by @ControllerAdvice and appropriate error response is returned back to the client.
 *
 * @see <a href="https://www.baeldung.com/spring-security-exceptionhandler#1configuring-authenticationentrypoint">Reference Article</a>
 * @see com.behl.flare.configuration.SecurityConfiguration
 * @see com.behl.flare.exception.TokenVerificationException
 * @see com.behl.flare.exception.ExceptionResponseHandler
 */
@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		handlerExceptionResolver.resolveException(request, response, null, new TokenVerificationException());
	}

}