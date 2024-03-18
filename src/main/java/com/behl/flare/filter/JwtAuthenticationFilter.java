package com.behl.flare.filter;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.behl.flare.utility.ApiEndpointSecurityInspector;
import com.google.firebase.auth.FirebaseAuth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * JwtAuthenticationFilter is a custom filter registered with the spring
 * security filter chain and works in conjunction with the security
 * configuration, as defined in {@link com.behl.flare.configuration.SecurityConfiguration}. 
 * 
 * It is responsible for verifying the authenticity of incoming HTTP requests to
 * secured API endpoints by verifying the received access token in the request header
 * and verifying it using the Firebase authentication service.
 * 
 * This filter is only executed for secure endpoints, and is skipped if the incoming
 * request is destined to a non-secured public API endpoint.
 *
 * @see com.behl.flare.utility.ApiEndpointSecurityInspector
 * @see com.behl.flare.configuration.FirebaseConfiguration
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final FirebaseAuth firebaseAuth;
	private final ApiEndpointSecurityInspector apiEndpointSecurityInspector;
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String USER_ID_CLAIM = "user_id";

	@Override
	@SneakyThrows
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		final var unsecuredApiBeingInvoked = apiEndpointSecurityInspector.isUnsecureRequest(request);
		
		if (Boolean.FALSE.equals(unsecuredApiBeingInvoked)) {
			final var authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
	
			if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX) ) {
				final var token = authorizationHeader.replace(BEARER_PREFIX, StringUtils.EMPTY);
				final var firebaseToken = firebaseAuth.verifyIdToken(token);
				final var userId = Optional.ofNullable(firebaseToken.getClaims().get(USER_ID_CLAIM)).orElseThrow(IllegalStateException::new);
				
				final var authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} 
		}
		filterChain.doFilter(request, response);
	}

}