package com.bits.pilani.restaurantservice.util;

import org.springframework.http.HttpStatus;

import com.bits.pilani.restaurantservice.config.GlobalWebConfig;
import com.bits.pilani.restaurantservice.exception.CustomException;
import com.bits.pilani.restaurantservice.security.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class TokenUtil {


    public static boolean validateUser(String token, int userId) throws CustomException
    {
        int userFromToken = 0;

        userFromToken = getUserIdFromToken(token);

        if(userFromToken == userId){
            return true;
        } else {
            Role role = getRoleFromToken(token);
            if(role.equals(Role.ADMIN)){
                return true;
            }
        }

        throw new CustomException(HttpStatus.UNAUTHORIZED, "Accessing other user's data is not permitted");
    }

    public static int getUserIdFromToken(String token) throws CustomException{
        
        Claims claims = getClaims(token);

        if(claims.containsKey("userId")){
            return (int) claims.get("userId");
        } else {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "No user found in the token!");
        } 
    }

    private static Claims getClaims(String token) throws CustomException{
        if (token.startsWith("Bearer")) {
            String tokenString = token.split(" ")[1];

            Claims claims = Jwts.parser().verifyWith(GlobalWebConfig.getSignInKey()).build().parseSignedClaims(tokenString).getPayload();
            return claims;
        } else {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Bearer token expected");
        }
    }

    public static Role getRoleFromToken(String token) throws CustomException{
        Claims claims = getClaims(token);
            
        if(claims.containsKey("role")){
            return Role.valueOf((String) claims.get("role"));
        } else {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "No role is assigned to this user");
        }

    }

}
