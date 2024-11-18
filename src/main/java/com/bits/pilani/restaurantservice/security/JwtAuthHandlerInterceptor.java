package com.bits.pilani.restaurantservice.security;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bits.pilani.restaurantservice.config.GlobalWebConfig;
import com.bits.pilani.restaurantservice.to.ErrorResponseTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthHandlerInterceptor implements HandlerInterceptor {
	
	ObjectMapper mapper = new ObjectMapper();

	private void sendBearerTokenNotFoundError(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponseTO errorResponse = new ErrorResponseTO();
		errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setError("BEARER_TOKEN_NOT_FOUND");
		errorResponse.setMessage("Bearer token is missing. Please provide JWT bearer token through Authorization header.");
		mapper.writeValue(response.getWriter(), errorResponse);		
	}

	private void sendInvalidRoleClaimError(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponseTO errorResponse = new ErrorResponseTO();
		errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
		errorResponse.setError("INVALID_ROLE_CLAIM");
		errorResponse.setMessage("Role claim in is invalid or missing.");				
		mapper.writeValue(response.getWriter(), errorResponse);						
	}
	
	private void sendAccessDeniedError(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponseTO errorResponse = new ErrorResponseTO();
		errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
		errorResponse.setError("ACCESS_DENIED");
		errorResponse.setMessage("User does not have permission to access this resource.");				
		mapper.writeValue(response.getWriter(), errorResponse);
	}
	
	private void sendInvalidTokenError(HttpServletResponse response) throws Exception {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponseTO errorResponse = new ErrorResponseTO();
		errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setError("INVALID_TOKEN");
		errorResponse.setMessage("Given bearer token in invalid. Please provide a valid token.");				
		mapper.writeValue(response.getWriter(), errorResponse);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (handler instanceof HandlerMethod handlerMethod) {
			Authorize authorize = handlerMethod.getMethodAnnotation(Authorize.class);
			
			if (authorize == null) {
				return true;				
			}
			
			String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (Objects.isNull(bearerToken)) {
				sendBearerTokenNotFoundError(response);
				return false;
			}

			if (bearerToken.startsWith("Bearer")) {
				String token = bearerToken.split(" ")[1];
				
				try {
					var claims = Jwts.parser().verifyWith(GlobalWebConfig.getSignInKey()).build().parseSignedClaims(token).getPayload();					

					if(!claims.containsKey("role")) {
						sendInvalidRoleClaimError(response);
						return false;
					}
					
					String roleName = String.class.cast(claims.get("role"));
					
					Role role = Role.valueOf(roleName);
										
					var isRoleAuthorized = Arrays.asList(authorize.roles()).contains(role);

					if(!isRoleAuthorized) {
						sendAccessDeniedError(response);
						return false;						
					}						
					
					
				} catch (JwtException e) {
					sendInvalidTokenError(response);
					return false;											
				} catch(IllegalArgumentException e) {
					sendInvalidRoleClaimError(response);
					return false;
				}
			}
		}

		return true;
	}
}
